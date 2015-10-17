package applicable.omnisim;

public class Vertex{
    public final int length,span;
    public final int inc,inc08,inc16,inc32;
    public final int end,end32,end16,end08;
    public Vertex(final int bytesPerIndex,final int pointerSpan,final int pointerCount,final int indexEnd){
        length=indexEnd;
        span=pointerSpan;
        inc  =(pointerCount);
        end  =(pointerCount*indexEnd);
        inc08=(bytesPerIndex*pointerSpan);                          inc16=inc08>>1;  inc32=inc08>>2;
        end08=(bytesPerIndex*pointerSpan)*(pointerCount*indexEnd);  end16=end08>>1;  end32=end08>>2;
    }
    /*
    public final int bpi,inc,length,stride,end32,end16,end08;
    public Vertex(final int bytesPerIndex,final int incrament,final int end){
        bpi=bytesPerIndex;
        inc=incrament;
        stride=incrament*bytesPerIndex;
        length=end;
        end08=incrament*bytesPerIndex*end;
        end16=end08>>1;
        end32=end08>>2;
    }
    */
}
