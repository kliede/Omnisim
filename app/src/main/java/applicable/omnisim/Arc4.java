package applicable.omnisim;

public class Arc4{
	private static final int Max=255;
	private static final int End=256;

	private int I,J,T;
	private int[] S=Hack.I32(End);

    public Arc4(final long key){  set(key);}

	public Arc4 set(final long key){
		final int bits=64;
        int i=0;
		for(I=0;I!=End;++I)S[I]=I;
		for(I=0,J=0;I!=End;++I){
			J=Max&(J+S[I]+(1&(int)(key>>>I)));
            T=S[I];  S[I]=S[J];  S[J]=T;
			if(++i==bits)i=0;
		}
		I=0;  J=0;
		return this;
	}

	public int  cypher08(){  J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  return S[(S[I]+S[J])&Max];}
	public int  cypher16(){  return cypher08()|(cypher08()<<8);}
	public int  cypher24(){  return cypher08()|(cypher08()<<8)|(cypher08()<<16);}
	public int  cypher32(){
        int i=0;
        J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max];
        J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]<< 8;
        J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]<<16;
        J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]<<24;
        return i;
    }
	public long cypher64(){  return cypher32()|((long)cypher32()<<32);}
	public int  cypher(final int bits){
        int i=0;
        if(bits> 0){  J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]    ;}
        if(bits> 8){  J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]<< 8;}
        if(bits>16){  J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]<<16;}
        if(bits>24){  J=Max&(J+S[I=Max&(I+1)]);  T=S[I];  S[I]=S[J];  S[J]=T;  i|=S[(S[I]+S[J])&Max]<<24;}
        return i&(int)((1L<<bits)-1L);
    }
}
