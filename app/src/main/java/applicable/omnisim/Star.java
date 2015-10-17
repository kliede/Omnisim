package applicable.omnisim;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;

public class Star{
    private static final String vertShaderCode=
        "uniform mat4 mvp;"+
        "attribute vec4 coord;"+
        "varying vec4 color;"+
        "void main(){"+
            "vec3 pos=vec3(coord.x,coord.y,coord.z);"+
            "gl_Position=mvp*vec4(coord.x,coord.y,coord.z,0.0);"+
            "gl_PointSize=1.0+(256.0*(480.0/sqrt(dot(pos,pos))));"+
            "color=vec4(1.0,1.0,1.0,coord.w);"+
        "}"
    ;
    private static final String fragShaderCode=
        "precision mediump float;"+
        "varying vec4 color;"+
        "void main(){gl_FragColor=color;}"
    ;
    private static final FloatBuffer floatBuffer;
    public  final int program,coordHandle,mvpHandle;
    public  static final Vertex vertex=new Vertex(4,4,1,Hack.StarEnd);
    public  static final ByteBuffer byteBuffer;
    public  static final byte[] byteArray;
    static{
        byteBuffer=ByteBuffer.allocateDirect(vertex.end08);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteArray=byteBuffer.array();
        floatBuffer=byteBuffer.asFloatBuffer();
        floatBuffer.position(0);
    }
    public Star(){
        int vertHandle,fragHandle;
        program=GLES20.glCreateProgram();                           Render.checkGlError("glCreateProgram");  Render.checkGlError(program);

        vertHandle=GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);  Render.checkGlError("glCreateShader");
        fragHandle=GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);Render.checkGlError("glCreateShader");
        GLES20.glShaderSource(vertHandle,vertShaderCode);           Render.checkGlError("glShaderSource");
        GLES20.glShaderSource(fragHandle,fragShaderCode);           Render.checkGlError("glShaderSource");
        GLES20.glCompileShader(vertHandle);                         Render.checkGlError("glCompileShader");
        GLES20.glCompileShader(fragHandle);                         Render.checkGlError("glCompileShader");
        GLES20.glAttachShader(program,vertHandle);                  Render.checkGlError("glAttachShader");
        GLES20.glAttachShader(program,fragHandle);                  Render.checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);                              Render.checkGlError("glLinkProgram");
                                                                    Render.checkGlShade(vertHandle);
                                                                    Render.checkGlShade(fragHandle);

        coordHandle=GLES20.glGetAttribLocation(program,"coord");    Render.checkGlError("glGetAttribLocation");
        mvpHandle=GLES20.glGetUniformLocation(program,"mvp");       Render.checkGlError("glGetUniformLocation");
    }
    public void draw(float[] mvp){
        GLES20.glUseProgram(program);                               Render.checkGlError("glUseProgram");


        floatBuffer.position(0);
        GLES20.glEnableVertexAttribArray(coordHandle);              Render.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(coordHandle,vertex.inc32,GLES20.GL_FLOAT,false,vertex.inc08,floatBuffer);
                                                                    Render.checkGlError("glVertexAttribPointer");

        Render.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mvpHandle,1,false,mvp,0);         Render.checkGlError("glUniformMatrix4fv");
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,vertex.end);       Render.checkGlError("glDrawArrays");

        GLES20.glDisableVertexAttribArray(coordHandle);             Render.checkGlError("glDisableVertexAttribArray");

    }
}
