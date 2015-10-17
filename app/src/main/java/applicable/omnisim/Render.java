package applicable.omnisim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Render implements GLSurfaceView.Renderer{
    public static final String TAG="Render";
    public static Disk disk;
    public static Star star;
    public static Line line;
    //<0>private Rays rays;
    private static Printer printer;
    private final float[] MVP=Hack.F32(16),MP=Hack.F32(16),MV=Hack.F32(16),MM=Hack.F32(16);
    public  static final V64 V64Eye=new V64(),V64Center=new V64(),V64Up=new V64();
    public  static final Q64 QO=MainRS.QO;
    @Override public void onSurfaceCreated(GL10 unused,EGLConfig config){
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);                                   Render.checkGlError("glClearColor");
        GLES20.glEnable(GLES20.GL_BLEND);                                           Render.checkGlError("glEnable");
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);      Render.checkGlError("glBlendFunc");
        disk=new Disk();
        star=new Star();
        line=new Line();
        //<0>rays=new Rays();
        printer =new Printer();
    }
    @Override public void onDrawFrame(GL10 unused){
        Profiler.profiler[Profiler.ProfilerEnd-3].Start("openGL");
        final float ratio=(float)Controls.XD/Controls.YD;
        final float zoom=(float)(1.0D/Controls.Zoom);
        V64Eye.Set(QO.ZAxis()).Mul(-1.0f);
        V64Center.Set(0,0,0);
        V64Up.Set(QO.YAxis());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);          Render.checkGlError("glClear");
        Matrix.frustumM(MP,0,-ratio*zoom,ratio*zoom,-zoom,zoom,1.0f,(float)(1L<<60));
        Matrix.setLookAtM(MV,0,
            (float)V64Eye.X   ,(float)V64Eye.Y   ,(float)V64Eye.Z   ,
            (float)V64Center.X,(float)V64Center.Y,(float)V64Center.Z,
            (float)V64Up.X    ,(float)V64Up.Y    ,(float)V64Up.Z
        );
        Matrix.multiplyMM(MVP,0,MP,0,MV,0);
        Profiler.profiler[Profiler.ProfilerEnd-6].Start("disk");
        disk.draw(MVP);
        Profiler.profiler[Profiler.ProfilerEnd-6].Stop();
        Profiler.profiler[Profiler.ProfilerEnd-5].Start("star");
        star.draw(MVP);
        Profiler.profiler[Profiler.ProfilerEnd-5].Stop();
        Profiler.profiler[Profiler.ProfilerEnd-7].Start("Hud");
        //<0>rays.draw();
        Hud.draw();
        line.draw();
        Profiler.profiler[Profiler.ProfilerEnd-7].Stop();

        printer.Enable();
        Profiler.profiler[Profiler.ProfilerEnd-4].Start("printer");
        final int e=Hack.Clamp(32,coutEnd,0);
        for(int i=0;i!=e;++i)printer.cout(coutText[i],coutColor[i]);
        Profiler.profiler[Profiler.ProfilerEnd-4].Stop();
        Profiler.profiler[Profiler.ProfilerEnd-3].Stop();
        printer.Disable();

    }
    @Override public void onSurfaceChanged(GL10 unused,int width,int height){
        GLES20.glViewport(0,0,width,height);                                    Render.checkGlError("glViewport");
        float ratio=(float)width/height;
        Matrix.frustumM(MP,0,-ratio,ratio,-1.0f,1.0f,1.0f,(float)(1L<<60));
    }

    public static void checkGlError(final int program){
        if(!GLES20.glIsProgram(program)){
            Log.e(TAG,"glIsProgram : glError false");
            throw new RuntimeException("glIsProgram : glError false");
        }
    }
    public static void checkGlError(String glOperation){
        int error;
        while((error=GLES20.glGetError())!=GLES20.GL_NO_ERROR){
            Log.e(TAG,glOperation+": glError "+error);
            throw new RuntimeException(glOperation+": glError "+error+" "+Hack.Hex(error,32));
        }
    }
    public static void checkGlShade(final int shaderHandle){
        final String s=GLES20.glGetShaderInfoLog(shaderHandle);
        if(s.length()>2)Log.e(TAG,s);
    }
    public static int[] loadTexture(final int program,final int w,final int h){
        final int end=w*h;
        final int[] handler=new int[1];
        final int[] texture=new int[end];

        for(int i=0;i!=end;++i){
            int x=(i%w)&1;
            int y=(i/w)&1;
            if(x==y)texture[i]=0xFF000000;
            if(x!=y)texture[i]=0xFF00FF00;
        }
        ByteBuffer textureBB=ByteBuffer.allocateDirect(end*4);
        textureBB.order(ByteOrder.nativeOrder());
        final IntBuffer textureBuffer=textureBB.asIntBuffer();
        textureBuffer.put(texture);  textureBuffer.position(0);

        GLES20.glUseProgram(program);           Render.checkGlError("glUseProgram");
        GLES20.glGenTextures(1,handler,0);      Render.checkGlError("glGenTextures");
        if(handler[0]!=0){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,handler[0]);  Render.checkGlError("glBindTexture");
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);  Render.checkGlError("glTexParameteri");
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);  Render.checkGlError("glTexParameteri");
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,w,h,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,textureBuffer);
            Render.checkGlError("glTexImage2D");
        }
        return handler;
    }
    public static int[] loadTexture(final int program,final Bitmap bitmap){
        final int[] handler=new int[1];
        GLES20.glUseProgram(program);           Render.checkGlError("glUseProgram");
        GLES20.glGenTextures(1,handler,0);      Render.checkGlError("glGenTextures");
        if(handler[0]!=0){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,handler[0]);  Render.checkGlError("glBindTexture");
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);  Render.checkGlError("glTexParameteri");
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);  Render.checkGlError("glTexParameteri");
            //GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);      Render.checkGlError("glGenerateMipmap");
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        }
        return handler;
    }
    private static int coutIndex=0,coutEnd=0;
    private static String coutText[]=Hack.ArrayString(64);
    private static int coutColor[]=Hack.I32(64);
    public static void coutReset(){  coutEnd=coutIndex;  coutIndex=0;}
    public static void cout(final String text,final int c){ coutColor[coutIndex]=         c;  coutText[coutIndex++]=   text;}
    public static void cout(final int text,final int c){    coutColor[coutIndex]=         c;  coutText[coutIndex++]=""+text;}
    public static void cout(final float text,final int c){  coutColor[coutIndex]=         c;  coutText[coutIndex++]=Hack.SpaceF64(text,7,3);}
    public static void cout(final double text,final int c){ coutColor[coutIndex]=         c;  coutText[coutIndex++]=Hack.SpaceF64(text,7,3);}
    public static void cout(final String text){             coutColor[coutIndex]=0xFFFFFFFF;  coutText[coutIndex++]=text;}
    public static void cout(final int text){                coutColor[coutIndex]=0xFFFFFFFF;  coutText[coutIndex++]=""+text;}
    public static void cout(final float text){              coutColor[coutIndex]=0xFFFFFFFF;  coutText[coutIndex++]=Hack.SpaceF64(text,7,3);}
    public static void cout(final double text){             coutColor[coutIndex]=0xFFFFFFFF;  coutText[coutIndex++]=Hack.SpaceF64(text,7,3);}
}