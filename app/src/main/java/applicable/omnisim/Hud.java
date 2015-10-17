package applicable.omnisim;

public class Hud{
    public static float YSet=0.0f,YPos=0.0f;
    public static void draw(){
        final float xB=0.9f,xA= 0.8f,xC=(xB+xA)*0.5f;
        final float yB=0.9f,yA=-0.9f,yC=yA+(YSet*(yB-yA)),yD=yA+(YPos*(yB-yA));

        Render.line.setColor(0xFFFFFFFF);
        Render.line.add(xC,yA,xC,yB);
        Render.line.add(xA,yA,xB,yA);
        Render.line.add(xA,yB,xB,yB);
        Render.line.setColor(0xFF00FF00);
        Render.line.add(xC,yC,xB,yC);
        Render.line.setColor(0xFFFFFF00);
        Render.line.add(xA,yD,xC,yD);
    }

}
