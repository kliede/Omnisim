package applicable.omnisim;
//this is my last and final will
//
public class Indicer{
    public static final int invalid=0xFFFFFFFF;
    public final int end,endV;
    public final int[] P,V,N;
    private int head,tail,count,index,direction;
    public Indicer(final int length){
        end=length;
        endV=(end+31)>>>5;
        P=Hack.I32(end);
        V=Hack.I32(endV);
        N=Hack.I32(end);
        init();
    }
    public void init(){
        head=0;  tail=end-1;  count=0;
        for(int i=0;i!=endV;++i)V[0]=0x00000000;
        for(int i=0;i!=end;++i){  P[i]=i-1;  N[i]=i+1;}  P[0]=tail;  N[tail]=0;
    }
    private int getValid(final int i){  return 1&(V[i>>>5]>>>(i&31));}
    private void setValid(final int i,final int v){  V[i>>>5]&=~(1<<(i&31));  V[i>>>5]|=(1&v)<<(i&31);}
    public void append(final int i,final int a){
        if(i<0||a<0||i>=end||a>=end)return;
        if(i!=a&&getValid(i)==getValid(a)){
            if(i==head)head=N[i];
            if(i==tail)tail=P[i];
            if(a==tail)tail=i;
            N[P[i]]=N[i];
            P[N[i]]=P[i];
            N[i]=N[a];
            P[i]=  a ;
            N[P[i]]=i;
            P[N[i]]=i;
        }
    }
    public void insert(final int i,final int b){
        if(i<0||b<0||i>=end||b>=end)return;
        if(i!=b&&getValid(i)==getValid(b)){
            if(i==head)head=N[i];
            if(i==tail)tail=P[i];
            if(b==head)head=i;
            N[P[i]]=N[i];
            P[N[i]]=P[i];
            N[i]=  b ;
            P[i]=P[b];
            N[P[i]]=i;
            P[N[i]]=i;
        }
    }
	public int add(){
		if(count==end)return invalid;  ++count;
        final int i=(tail=N[tail]);
        setValid(i,1);
		return i;
	}
	public boolean sub(final int i){
		if(i<0||i>=end||0==getValid(i))return false;
        --count;
        setValid(i,0);
        if(head==i&&count!=0)head=N[head];
        if(tail==i){  tail=P[tail];}else{
            N[P[i]]=N[i];
            P[N[i]]=P[i];
            final int t=  tail ;
            final int b=N[tail];
            P[i]=t;  N[t]=i;
            N[i]=b;  P[b]=i;
        }
		return true;
	}
    public boolean valid(final int i){  return i>=0&&i<end&1==(1&(V[i>>>5]>>>(i&31)));}
    public boolean empty(){  return count==0;}
    public boolean full(){  return count==end;}
    public int head(){  return (!empty()?head:invalid);}
    public int tail(){  return (!empty()?tail:invalid);}
    public int count(){  return count;}
    public boolean prep(final int i[],final boolean headToTail){
        index=-1;
        if(headToTail){  i[0]=P[head];  direction=1;}else{  i[0]=N[tail];  direction=-1;}
        return count!=0;
    }
    public int next(final int i){  return N[i];}
    public int prev(final int i){  return P[i];}
    public boolean next(final int i[]){
        if(++index>=count)return false;
        i[0]=(direction==1?N[i[0]]:P[i[0]]);
        return true;
    }

    public String print(){
        int a=head;
        int b=tail;
        int x=-1,y=-1,z=count;
        for(int i=0;i!=end;++i)x+=(head==(a=N[a])?1:0);
        for(int i=0;i!=end;++i)y+=(tail==(b=P[b])?1:0);
        for(int i=0;i!=end;++i)z-=getValid(i);
        return Hack.Space(count,3)+"/"+Hack.Space(end,3)+"  "+Hack.Space(x,3)+"  "+Hack.Space(y,3)+"  "+Hack.Space(z,3);
    }
}
