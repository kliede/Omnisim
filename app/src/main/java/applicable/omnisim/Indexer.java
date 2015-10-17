package applicable.omnisim;

public class Indexer{
    public static final int invalid=0xFFFFFFFF;
    public final int end;
    public final int[] data;
    public int stop;
    public Indexer(final int length){
        end=length;
        data=Hack.I32(end);
        init();
    }
    public void init(){  stop=0;  for(int i=0;i!=end;++i)data[i]=i;}
	public int add(){
		if(stop==end)return invalid;
        final int i=data[stop++];
		return i;
	}
	public boolean sub(final int a){
		if(a<0||a>=stop)return false;
		int b=--stop;
		if(a!=b){  data[a]^=data[b];  data[b]^=data[a];  data[a]^=data[b];}
		return true;
	}
    public int getTail(){  return (stop==0?invalid:stop-1);}
    public boolean isFull(){  return stop==end;}
    public String print(){  return stop+"/"+end+" "+data[0]+" "+data[1]+" "+data[2]+" "+data[3];}
}
