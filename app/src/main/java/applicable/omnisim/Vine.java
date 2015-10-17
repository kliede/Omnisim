package applicable.omnisim;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Vine{
    private static final String vertShaderCode=
        "uniform mat4 mvp;"+
        "attribute vec4 coord;"+
        "void main(){"+
            "gl_Position=mvp*coord;"+
        "}"
    ;
    private static final String fragShaderCode=
        "precision mediump float;"+
        "void main(){gl_FragColor=vec4(1.0,1.0,1.0,1.0);}"
    ;
    private static final float[] MVP=Hack.F32(16),MP=Hack.F32(16),MV=Hack.F32(16),MM=Hack.F32(16);
    private static final FloatBuffer floatBuffer;
    public  final int program,coordHandle,mvpHandle;
    public  static final Vertex vertex=new Vertex(4,3,2,256);
    public  static final ByteBuffer byteBuffer;
    public  static final byte[] byteArray;
    static{
        byteBuffer=ByteBuffer.allocateDirect(vertex.end08);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteArray=byteBuffer.array();
        floatBuffer=byteBuffer.asFloatBuffer();
        floatBuffer.position(0);
    }
    public static Vine Static;
    public Vine(){
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
        mvpHandle=GLES20.glGetUniformLocation(program,"mvp");       Render.checkGlError("glGetUniformLocation");

        GLES20.glUseProgram(program);                               Render.checkGlError("glUseProgram");
    }
    private static final float aspect=((float)Controls.XD)/((float)Controls.YD);
    public void setZoom(float x,float y){
        Matrix.frustumM(MP,0,-x,x,-y,y,1.0f,(float)(1L<<32));
        Matrix.setLookAtM(MV,0,0.0f,0.0f,-1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f);
        Matrix.multiplyMM(MVP,0,MP,0,MV,0);
    }
    private static int end=0;
    public static void reset(){  end=0;}
    public void add(float xA,float yA,float zA,float xB,float yB,float zB){
        if(end!=vertex.end){
            float data[]={xA,yA,zA,xB,yB,zB};
            floatBuffer.position(vertex.span*end);
            floatBuffer.put(data,0,vertex.span*vertex.inc);
            end+=vertex.inc;
        }
    }
    public void draw(){
        if(end==0)return;
        GLES20.glUseProgram(program);                               Render.checkGlError("glUseProgram");

        floatBuffer.position(0);
        GLES20.glEnableVertexAttribArray(coordHandle);              Render.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(coordHandle,vertex.inc32,GLES20.GL_FLOAT,false,vertex.inc08,floatBuffer);

        setZoom(1.0f,1.0f);
        GLES20.glUniformMatrix4fv(mvpHandle,1,false,MVP,0);         Render.checkGlError("glUniformMatrix4fv");
        GLES20.glDrawArrays(GLES20.GL_LINES,0,end);
                                                                    Render.checkGlError("glDrawArrays");

        GLES20.glDisableVertexAttribArray(coordHandle);             Render.checkGlError("glDisableVertexAttribArray");
        reset();
    }
}
