package applicable.omnisim;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Printer{
    private final String vertShaderCode=
        "uniform mat4 mvp;"+
        "uniform vec4 offset;"+
        "uniform vec2 trans;"+
        "attribute vec4 coord;"+
        "attribute vec2 textu;"+
        "varying vec2 textuV;"+
        "void main(){gl_Position=(mvp*coord)+offset;textuV=textu+trans;}"
    ;
    private final String fragShaderCode=
        "precision mediump float;"+
        "uniform sampler2D texture;"+
        "uniform vec4 color;"+
        "varying vec2 textuV;"+
        "void main(){gl_FragColor=color*texture2D(texture,textuV);}"
    ;
    private static final float xScale=1.0f/16.0f,yScale=1.0f/16.0f;
    private static final float[] MVP=Hack.F32(16),MP=Hack.F32(16),MV=Hack.F32(16),MM=Hack.F32(16);
    private static final float[] trans={0.0f,0.0f};
    private static final float[] color={0.5f,0.5f,0.5f,1.0f};
    private static final float[] offset={0.0f,0.0f,0.0f,0.0f};
    private static final float aspect=((float)Controls.XD)/((float)Controls.YD);
    private static float xSpace=xScale,ySpace=yScale;
    private static final float coord[]={
         1.0f, 1.0f,0.0f,
        -1.0f, 1.0f,0.0f,
         1.0f,-1.0f,0.0f,
        -1.0f,-1.0f,0.0f,
         1.0f,-1.0f,0.0f,
        -1.0f, 1.0f,0.0f
    };
    private static final float textu[]={
          0.0f,  0.0f,
        xScale,  0.0f,
          0.0f,yScale,
        xScale,yScale,
          0.0f,yScale,
        xScale,  0.0f
    };
    private final FloatBuffer coordBuffer,textuBuffer;
    public  final int program,coordHandle,textuHandle,colorHandle,offsetHandle,mvpHandle,textureHandle,transHandle;
    private final int[] textureID;

    public  static final Vertex vertexA=new Vertex(4,3,3,2);
    public  static final Vertex vertexB=new Vertex(4,2,2,3);

    public Printer(){
        final ByteBuffer coordBB=ByteBuffer.allocateDirect(vertexA.end08);
        final ByteBuffer textuBB=ByteBuffer.allocateDirect(vertexB.end08);
        coordBB.order(ByteOrder.nativeOrder());  coordBuffer=coordBB.asFloatBuffer();
        textuBB.order(ByteOrder.nativeOrder());  textuBuffer=textuBB.asFloatBuffer();
        coordBuffer.put(coord);  coordBuffer.position(0);
        textuBuffer.put(textu);  textuBuffer.position(0);

        int vertHandle,fragHandle;
        program=GLES20.glCreateProgram();                                   Render.checkGlError("glCreateProgram");  Render.checkGlError(program);

        vertHandle=GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);          Render.checkGlError("glCreateShader");
        fragHandle=GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);        Render.checkGlError("glCreateShader");
        GLES20.glShaderSource(vertHandle,vertShaderCode);                   Render.checkGlError("glShaderSource");
        GLES20.glShaderSource(fragHandle,fragShaderCode);                   Render.checkGlError("glShaderSource");
        GLES20.glCompileShader(vertHandle);                                 Render.checkGlError("glCompileShader");
        GLES20.glCompileShader(fragHandle);                                 Render.checkGlError("glCompileShader");
        GLES20.glAttachShader(program,vertHandle);                          Render.checkGlError("glAttachShader");
        GLES20.glAttachShader(program,fragHandle);                          Render.checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);                                      Render.checkGlError("glLinkProgram");
                                                                            Render.checkGlShade(vertHandle);
                                                                            Render.checkGlShade(fragHandle);

        textureID=Render.loadTexture(program,MainActivity.text);

        coordHandle=GLES20.glGetAttribLocation(program,"coord");            Render.checkGlError("glGetAttribLocation");
        textuHandle=GLES20.glGetAttribLocation(program,"textu");            Render.checkGlError("glGetAttribLocation");
        colorHandle=GLES20.glGetUniformLocation(program,"color");           Render.checkGlError("glGetUniformLocation");
        offsetHandle=GLES20.glGetUniformLocation(program,"offset");         Render.checkGlError("glGetUniformLocation");
        transHandle=GLES20.glGetUniformLocation(program,"trans");           Render.checkGlError("glGetUniformLocation");
        mvpHandle=GLES20.glGetUniformLocation(program,"mvp");               Render.checkGlError("glGetUniformLocation");
        textureHandle=GLES20.glGetUniformLocation(program,"texture");       Render.checkGlError("glGetUniformLocation");

        GLES20.glUseProgram(program);                                       Render.checkGlError("glUseProgram");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);                         Render.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureID[0]);            Render.checkGlError("glBindTexture");
        GLES20.glUniform1i(textureHandle,0);                                Render.checkGlError("glUniform1i");
        GLES20.glUniform4fv(colorHandle,1,color,0);                         Render.checkGlError("glUniform4fv");

    }
    public void setZoom(float x,float y){
        Matrix.frustumM(MP,0,-x*aspect,x*aspect,-y,y,1.0f,(float)(1L<<32));
        Matrix.setLookAtM(MV,0,0.0f,0.0f,-1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f);
        Matrix.multiplyMM(MVP,0,MP,0,MV,0);
        xSpace=(16.0f/x)*xScale;
        ySpace=(16.0f/y)*yScale*aspect*1.25f;
    }
    public static boolean Enabled=false;
    public void Enable(){
        Enabled=true;
        GLES20.glUseProgram(program);                                       Render.checkGlError("glUseProgram");
        setZoom(24.0f,24.0f);
        offset[0]=1.0f-(0.5f*xSpace);
        offset[1]=1.0f-(0.5f*ySpace);
        offset[2]=1.0f;
        offset[3]=0.0f;

        textuBuffer.position(0);
        coordBuffer.position(0);

        GLES20.glEnableVertexAttribArray(coordHandle);                      Render.checkGlError("glEnableVertexAttribArray");
        GLES20.glEnableVertexAttribArray(textuHandle);                      Render.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(coordHandle,vertexA.inc32,GLES20.GL_FLOAT,false,vertexA.inc08,coordBuffer);
                                                                            Render.checkGlError("glVertexAttribPointer");
        GLES20.glVertexAttribPointer(textuHandle,vertexB.inc32,GLES20.GL_FLOAT,false,vertexB.inc08,textuBuffer);


        GLES20.glUniformMatrix4fv(mvpHandle,1,false,MVP,0);                 Render.checkGlError("glUniformMatrix4fv");
    }
    public void Disable(){
        GLES20.glDisableVertexAttribArray(coordHandle);
        GLES20.glDisableVertexAttribArray(textuHandle);
        Enabled=false;
    }
    public void setColor(final int c){
        final float invert=1.0f/(float)0xFF;
        if(Enabled&&currentColor!=c){
            currentColor=c;
            color[3]=invert*(0xFF&(c>>>24));
            color[2]=invert*(0xFF&(c>>>16));
            color[1]=invert*(0xFF&(c>>> 8));
            color[0]=invert*(0xFF&(c>>> 0));
            GLES20.glUniform4fv(colorHandle,1,color,0);                         Render.checkGlError("glUniform4fv");
        }
    }
    private static int currentColor=0x00000000;
    public void cout(final String text,final int c){
        if(!Enabled)return;
        setColor(c);
        final int e=Hack.Clamp(63,text.length(),0);
        for(int i=e;--i>-1;){
            final int index=((int)text.charAt(i));
            trans[0]=xScale*(index%16);
            trans[1]=yScale*(index/16);
                                                                                Render.checkGlError("glVertexAttribPointer");
            GLES20.glUniform4fv(offsetHandle,1,offset,0);                       Render.checkGlError("glUniform4fv");
            GLES20.glUniform2fv(transHandle,1,trans,0);                         Render.checkGlError("glUniform2fv");
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexA.end);             Render.checkGlError("glDrawArrays");
            offset[0]-=xSpace;
        }
        offset[0]=1.0f-(0.5f*xSpace);
        offset[1]-=ySpace;
    }
}
