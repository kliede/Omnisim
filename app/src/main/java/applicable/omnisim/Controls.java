package applicable.omnisim;

public class Controls{
    private static final int ButtonsEnd=1;
    private static final int TouchesEnd=8;
    private static final Binder BinderA=new Binder(ButtonsEnd,TouchesEnd);
    private static final Binder BinderB=new Binder(ButtonsEnd,TouchesEnd);
    public  static final Q64 QO=new Q64(),QV=new Q64();
    private static final V64 VZ=new V64();

    private static final double ZoomMax=(double)0x003FFFFF,ZoomMin=1.0D;
    public static final int XD=MainActivity.XD,XM=XD-1,XR=XD>>>1;
    public static final int YD=MainActivity.YD,YM=YD-1,YR=YD>>>1;
    public static final int RD=XD>YD?XD:YD,RR=RD>>>1,RQ=RR>>>1,RM=RD-1;
    public static final int Pixels=XD*YD;
    public static double Zoom=ZoomMin;
    public static double Focus=Zoom*RR;
    private static long WarpCycle=0L;

    private static double SliceMod(final double s){  return 0.25D*(s+(s*s)+(s*s*s)+(s*s*s*s));}
    public static void SetZoom(final double f){
        Zoom*=1.0D+(f*SliceMod(Clock.Slice));
        Zoom=Hack.Clamp(ZoomMax,Zoom,ZoomMin);
		Focus=Zoom*RR;
	}
    public static void SetZoom(){
        Zoom=ZoomMin;
		Focus=Zoom*RR;
	}

    private static Button[] Buttons={
            //new Button(1.00D,0.75D,0.50D,0.00D,Button.FlagCircle|Button.FlagUnitXY|Button.FlagVector),
            //new Button(1.00D,0.75D,1.00D,0.50D,Button.FlagCircle|Button.FlagUnitXY|Button.FlagVector),
            //new Button(1.00D,0.75D,0.50D,0.00D,Button.FlagCircle|Button.FlagUnitXY|Button.FlagVector),
            new Button(1.00D,0.00D,1.00D,0.00D,Button.FlagSquare|Button.FlagClipXY)
    };

    public static void TouchBgn(final int b,double x,double y){
        x=XM-x;
        y=YM-y;
        for(int a=0;a!=ButtonsEnd;++a){
            if(Buttons[a].Covers(x,y)){
                if(BinderA.Bind(a,b)){  Buttons[a].Bgn(x,y, true);}else
                if(BinderB.Bind(a,b)){  Buttons[a].Bgn(x,y,false);}
                break;
            }
        }
    }

    public static void TouchMov(final int b,double x,double y){
        x=XM-x;
        y=YM-y;
        if(BinderA.BoundB[b])Buttons[BinderA.AfromB[b]].Now(x,y, true);
        if(BinderB.BoundB[b])Buttons[BinderB.AfromB[b]].Now(x,y,false);
    }

    public static void TouchEnd(final int b){
        if(BinderA.BoundB[b]){  Buttons[BinderA.AfromB[b]].End( true);  BinderA.ReleaseB(b);}
        if(BinderB.BoundB[b]){  Buttons[BinderB.AfromB[b]].End(false);  BinderB.ReleaseB(b);}
    }
    public static double Sensitivity=2.0D;
    public static int warpX=0,warpY=0,warpZ=0;
    public static int Clicked=0;
    public static double YSet=0.0D,YPos=0.0D,YVel=0.0D,YAcc=0.0D;

    public static boolean Intercept(){
        final double maxAcc=1.0f/32.0f;
        final double p=YSet-YPos;
        final double v=YVel;
        final double r=Hack.ABS(p);
        final double maxVel=(p<0?-1:1)*Hack.Sqrt((r*maxAcc)+(0.5D*(v*v)));
        //final float maxVel=(p<0?-1:1)*Hack.Sqrt((r*maxAcc)+(0.5f*((v*v)+(0.0f*0.0f))));
        final double timeToPos=p/(0.5D*maxVel);
        final double timeToDec=v/(0.5D*maxAcc);
        final double t=Hack.Clamp(1.0D,timeToPos,0.0D);
        YAcc=maxAcc*(maxVel-v<0?-1:1);
/*
        Render.cout("");
        Render.cout("p: "+Hack.SpaceF64(p),0xFFFFFFFF);
        Render.cout("v: "+Hack.SpaceF64(v),0xFFFFFFFF);
        Render.cout("");
        Render.cout("V: "+Hack.SpaceF64(maxVel),0xFFFFFFFF);
        Render.cout("r: "+Hack.SpaceF64(r),0xFFFFFFFF);
        Render.cout("timeToPos: "+Hack.SpaceF64(timeToPos),0xFFFFFFFF);
        Render.cout("timeToDec: "+Hack.SpaceF64(timeToDec),0xFFFFFFFF);
*/

        return true;
    }

    private static void Look(final int i){
        YSet=Buttons[i].YLoc;
        if(BinderA.BoundA[i]||BinderB.BoundA[i]){
            if(Buttons[i].Dragged!=-2){
                QO.PYR(V64.Get(Buttons[i].Y/Focus,Buttons[i].X/Focus,Buttons[i].Roll/Sensitivity).Mul(Sensitivity));
                QV.Set(QO).Conj();
                if(BinderA.BoundA[i]&&BinderB.BoundA[i]){
                    final double zoom=Buttons[i].Zoom/Buttons[i].ZoomLast;
                    Buttons[i].ZoomLast=Buttons[i].Zoom;
                    Zoom*=zoom;
                    Zoom=Hack.Clamp(ZoomMax,Zoom,ZoomMin);
                    Focus=Zoom*RR;
                }
            }
        }
    }
    private static void Zoomer(final int a){
        if(BinderA.BoundA[a]||BinderB.BoundA[a]){
            if(0.1D<Hack.ABS(Buttons[a].Y))SetZoom(Buttons[a].Y*8.0D);
        }
    }
    public static void Update(){
        for(int a=0;a!=ButtonsEnd;++a)Buttons[a].Update();
        int i=-1;
        //Zoomer(++i);
        Look(++i);
        VZ.Set(QO.ZAxis()).Norm();

        if(YPos>=YSet){  YPos=YSet;  YVel=0.0D;  YAcc=0.0D;}else{
            final double delta=1.0D/32.0D;
            Intercept();
            YPos+=YVel*delta;
            YVel+=YAcc*delta;
            YPos+=YAcc*delta*delta*0.5D;
            YPos=Hack.Clamp(1.0D,YPos,0.0D);
        }

        final int e=(int)(0x0000007F*(float)Hack.Clamp(1.0D,Hack.ABS(YPos),0.0D));//wasteful//
        final int x=(int)(0x00FFFFFF*(float)Hack.Clamp(1.0D,Hack.ABS(VZ.X),0.0D));
        final int y=(int)(0x00FFFFFF*(float)Hack.Clamp(1.0D,Hack.ABS(VZ.Y),0.0D));
        final int z=(int)(0x00FFFFFF*(float)Hack.Clamp(1.0D,Hack.ABS(VZ.Z),0.0D));
        warpX=(VZ.X<0.0D?0x80000000:0)|(e<<24)|x;
        warpY=(VZ.Y<0.0D?0x80000000:0)|(e<<24)|y;
        warpZ=(VZ.Z<0.0D?0x80000000:0)|(e<<24)|z;

        Hud.YSet=(float)YSet;
        Hud.YPos=(float)YPos;
    }
}
