package applicable.omnisim;

import android.app.Activity;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.Type;


public class MainRS{
    private final RenderScript rS;
    private final ScriptC_Shader script;
    private final Allocation allocationDisk;
    private final Allocation allocationStar;
    private final Allocation allocationV32P;
    private final Allocation AllocGalaxy;
    private final Allocation AllocStar;
    public  static final Q64 QO=new Q64();

    private static int[] GetIndexArr(final int end){  final int[] arr=Hack.I32(end);  for(int i=0;i!=end;++i)arr[i]=i;  return arr;}
    public MainRS(final Activity main){
        rS=RenderScript.create(main);
        script=new ScriptC_Shader(rS);
        AllocGalaxy=Allocation.createTyped(rS,new Type.Builder(rS,Element.U32(rS)).setX(Hack.GalaxyEnd).create());  AllocGalaxy.copyFrom(GetIndexArr(Hack.GalaxyEnd));
        AllocStar  =Allocation.createTyped(rS,new Type.Builder(rS,Element.U32(rS)).setX(Hack.StarEnd  ).create());    AllocStar.copyFrom(GetIndexArr(Hack.StarEnd  ));
        allocationDisk=Allocation.createTyped(rS,new Type.Builder(rS,Element.U8(rS)).setX(Disk.vertex.end08).create());
        allocationStar=Allocation.createTyped(rS,new Type.Builder(rS,Element.U8(rS)).setX(Star.vertex.end08).create());
        allocationV32P=Allocation.createTyped(rS,new Type.Builder(rS,Element.F32(rS)).setX(4).create());
        allocationDisk.copyFrom(Disk.byteArray);  script.set_allocationDisk(allocationDisk);
        allocationStar.copyFrom(Star.byteArray);  script.set_allocationStar(allocationStar);
        allocationV32P.copyFrom(Disk.offset   );  script.set_allocationV32P(allocationV32P);
        script.invoke_RunFirst(Controls.XD,Controls.YD);
    }
    public int genDisk(byte[] byteArray,int i){
        Profiler.profiler[++i].Start("genDisk");
        script.forEach_Galaxy(AllocGalaxy);         rS.finish();
        allocationDisk.copyTo(byteArray);           rS.finish();
        rS.finish();  Profiler.profiler[i].Stop();
        return i;
    }
    public int genStar(byte[] byteArray,int i){
        Profiler.profiler[++i].Start("genStar");
        script.forEach_Star(AllocStar);             rS.finish();
        allocationStar.copyTo(byteArray);           rS.finish();
        Profiler.profiler[i].Stop();
        return i;
    }
    public static boolean genGalaxy=true;
    public void Update(){
        int i=Profiler.index;

        Profiler.profiler[++i].Start("Update");
        script.invoke_Update(
            0xFFFFFFFFL&Clock.Millis,
            0xFFFFFFFFL&Controls.warpX,
            0xFFFFFFFFL&Controls.warpY,
            0xFFFFFFFFL&Controls.warpZ
        );
        rS.finish();
        allocationV32P.copyTo(Disk.offset);
        rS.finish();
        Profiler.profiler[i].Stop();

        if(genGalaxy)genDisk(Disk.byteArray,i);  ++i;  genGalaxy=false;
        genStar(Star.byteArray,i);
        QO.Set(Controls.QO);
        Profiler.index=i;
    }
}
