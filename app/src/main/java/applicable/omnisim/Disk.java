package applicable.omnisim;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Disk{
    private static final String vertShaderCode=
        "uniform mat4 mvp;"+
        "uniform vec4 offset;"+
        "uniform vec3 colors[8];"+
        "uniform float focus;"+
        "attribute vec4 coord;"+
        "attribute vec3 alpha;"+
        "varying vec4 color;"+
        "void main(){"+
            "vec4 pos=coord+offset;"+
            "gl_Position=mvp*pos;"+
            "gl_PointSize=alpha.z+(256.0*(focus/sqrt(dot(pos,pos))));"+
            "color=vec4(colors[int(alpha.x)],alpha.y);"+
        "}"
    ;
    private static final String fragShaderCode=
        "precision mediump float;"+
        "varying vec4 color;"+
        "void main(){gl_FragColor=color;}"
    ;
    private static final FloatBuffer floatBuffer;
    public  final int program,alphaHandle,coordHandle,focusHandle,mvpHandle,offsetHandle,colorsHandle;
    public  static final Vertex vertex=new Vertex(4,6,1,Hack.GalaxyEnd);
    public  static final ByteBuffer byteBuffer;
    public  static final byte[] byteArray;
    public  static final float[] offset={0.0f,0.0f,0.0f,0.0f};
    private static final float[] colors={
        0x9B/255.0f,0xB0/255.0f,0xFF/255.0f,
        0x9B/255.0f,0xB0/255.0f,0xFF/255.0f,
        0xAA/255.0f,0xBF/255.0f,0xFF/255.0f,
        0xCA/255.0f,0xD7/255.0f,0xFF/255.0f,
        0xF8/255.0f,0xF7/255.0f,0xFF/255.0f,
        0xFF/255.0f,0xF4/255.0f,0xEA/255.0f,
        0xFF/255.0f,0xD2/255.0f,0xA1/255.0f,
        0xFF/255.0f,0xCC/255.0f,0x6F/255.0f
    };
    static{
        byteBuffer=ByteBuffer.allocateDirect(vertex.end08);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteArray=byteBuffer.array();
        floatBuffer=byteBuffer.asFloatBuffer();
        floatBuffer.position(0);
    }
    public static Disk Static;
    public Disk(){
        Static=this;
        program=GLES20.glCreateProgram();                           Render.checkGlError("glCreateProgram");  Render.checkGlError(program);
        int vertHandle,fragHandle;

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
        alphaHandle=GLES20.glGetAttribLocation(program,"alpha");    Render.checkGlError("glGetAttribLocation");
        focusHandle=GLES20.glGetUniformLocation(program,"focus");   Render.checkGlError("glGetUniformLocation");
        offsetHandle=GLES20.glGetUniformLocation(program,"offset"); Render.checkGlError("glGetUniformLocation");
        colorsHandle=GLES20.glGetUniformLocation(program,"colors"); Render.checkGlError("glGetUniformLocation");
        mvpHandle=GLES20.glGetUniformLocation(program,"mvp");       Render.checkGlError("glGetUniformLocation");

        GLES20.glUseProgram(program);                               Render.checkGlError("glUseProgram");
        GLES20.glUniform3fv(colorsHandle,8,colors,0);               Render.checkGlError("glUniform3fv");
    }
    public void draw(float[] mvp){
        GLES20.glUseProgram(program);                               Render.checkGlError("glUseProgram");

        floatBuffer.position(0);
        GLES20.glEnableVertexAttribArray(coordHandle);              Render.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(coordHandle,3,GLES20.GL_FLOAT,false,vertex.inc08,floatBuffer);
                                                                    Render.checkGlError("glVertexAttribPointer");
        floatBuffer.position(3);
        GLES20.glEnableVertexAttribArray(alphaHandle);              Render.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(alphaHandle,4,GLES20.GL_FLOAT,false,vertex.inc08,floatBuffer);
                                                                    Render.checkGlError("glVertexAttribPointer");

        GLES20.glUniform1f(focusHandle,(float)Controls.Focus);      Render.checkGlError("glUniform1f");
        GLES20.glUniform4fv(offsetHandle,1,offset,0);               Render.checkGlError("glUniform4fv");

        GLES20.glUniformMatrix4fv(mvpHandle,1,false,mvp,0);         Render.checkGlError("glUniformMatrix4fv");
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,vertex.end);         Render.checkGlError("glDrawArrays");

        GLES20.glDisableVertexAttribArray(coordHandle);             Render.checkGlError("glDisableVertexAttribArray");
        GLES20.glDisableVertexAttribArray(alphaHandle);             Render.checkGlError("glDisableVertexAttribArray");

    }
}
