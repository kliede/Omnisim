
#pragma version(1)
#pragma rs java_package_name(applicable.omnisim)
#pragma rs_fp_relaxed
//#include "stdbool.h"


typedef    int8_t I08;
typedef   uint8_t U08;
typedef   int16_t I16;
typedef  uint16_t U16;
typedef   int32_t I32;
typedef  uint32_t U32;
typedef   int64_t I64;
typedef  uint64_t U64;
typedef     float F32;
typedef    double F64;
typedef      U32 I128[4];
typedef    float3 V32;
typedef struct{   U08 x,y,z;}P08;
typedef struct{   U64 x,y,z;}P64;
typedef struct{  I128 x,y,z;}P128;
typedef struct{  P128 p,v,a;  I32 millis;}T128;
typedef struct{  I128 x,y,z,w;}Q128;
typedef struct{   V32 r,i;  I32 s[3];}Ray;
typedef rs_allocation Allocation;

static const U32 Invalid=0xFFFFFFFF;
static const U08 MaxBitG=127,MaxBitY= 99,MaxBitS=82,MaxBitM=57,MaxBitN=29;
static const U08 BgnBitG=100,BgnBitY= 83,BgnBitS=58,BgnBitM=30,BgnBitN= 0;
static const U08 StartBitStar=120;
static const U08  StopBitStar= 87;
static const U08   SumBitStar=StartBitStar-StopBitStar;
static const U32 StarEnd=27*SumBitStar;
static const U32 GalaxyEnd=1<<15;
static const U32 GalaxyStarEnd=GalaxyEnd+StarEnd;

static T128 T128Camera;
static U32 Millis=0;
static P64 StarSeedA[     27]={0};
static P64 StarSeedB[StarEnd]={0};

#define AllocSetF32 rsSetElementAt_float
#define AllocGetF32 rsGetElementAt_float
#define AllocSetU32 rsSetElementAt_uint
#define AllocGetU32 rsGetElementAt_uint
#define AllocSetU08 rsSetElementAt_uchar
#define AllocGetU08 rsGetElementAt_uchar
#define PI1 3.1415926535897932384626433832795f
#define PI2 6.283185307179586476925286766559f
#define PI1Inv 0.31830988618379067153776752674503f
#define PI2Inv 0.15915494309189533576888376337251f

Allocation allocationDisk;
Allocation allocationStar;
Allocation allocationV32P;

static const U32 WeaveFF[256]={
    0x0000,0x0001,0x0004,0x0005,0x0010,0x0011,0x0014,0x0015,0x0040,0x0041,0x0044,0x0045,0x0050,0x0051,0x0054,0x0055,
    0x0100,0x0101,0x0104,0x0105,0x0110,0x0111,0x0114,0x0115,0x0140,0x0141,0x0144,0x0145,0x0150,0x0151,0x0154,0x0155,
    0x0400,0x0401,0x0404,0x0405,0x0410,0x0411,0x0414,0x0415,0x0440,0x0441,0x0444,0x0445,0x0450,0x0451,0x0454,0x0455,
    0x0500,0x0501,0x0504,0x0505,0x0510,0x0511,0x0514,0x0515,0x0540,0x0541,0x0544,0x0545,0x0550,0x0551,0x0554,0x0555,
    0x1000,0x1001,0x1004,0x1005,0x1010,0x1011,0x1014,0x1015,0x1040,0x1041,0x1044,0x1045,0x1050,0x1051,0x1054,0x1055,
    0x1100,0x1101,0x1104,0x1105,0x1110,0x1111,0x1114,0x1115,0x1140,0x1141,0x1144,0x1145,0x1150,0x1151,0x1154,0x1155,
    0x1400,0x1401,0x1404,0x1405,0x1410,0x1411,0x1414,0x1415,0x1440,0x1441,0x1444,0x1445,0x1450,0x1451,0x1454,0x1455,
    0x1500,0x1501,0x1504,0x1505,0x1510,0x1511,0x1514,0x1515,0x1540,0x1541,0x1544,0x1545,0x1550,0x1551,0x1554,0x1555,
    0x4000,0x4001,0x4004,0x4005,0x4010,0x4011,0x4014,0x4015,0x4040,0x4041,0x4044,0x4045,0x4050,0x4051,0x4054,0x4055,
    0x4100,0x4101,0x4104,0x4105,0x4110,0x4111,0x4114,0x4115,0x4140,0x4141,0x4144,0x4145,0x4150,0x4151,0x4154,0x4155,
    0x4400,0x4401,0x4404,0x4405,0x4410,0x4411,0x4414,0x4415,0x4440,0x4441,0x4444,0x4445,0x4450,0x4451,0x4454,0x4455,
    0x4500,0x4501,0x4504,0x4505,0x4510,0x4511,0x4514,0x4515,0x4540,0x4541,0x4544,0x4545,0x4550,0x4551,0x4554,0x4555,
    0x5000,0x5001,0x5004,0x5005,0x5010,0x5011,0x5014,0x5015,0x5040,0x5041,0x5044,0x5045,0x5050,0x5051,0x5054,0x5055,
    0x5100,0x5101,0x5104,0x5105,0x5110,0x5111,0x5114,0x5115,0x5140,0x5141,0x5144,0x5145,0x5150,0x5151,0x5154,0x5155,
    0x5400,0x5401,0x5404,0x5405,0x5410,0x5411,0x5414,0x5415,0x5440,0x5441,0x5444,0x5445,0x5450,0x5451,0x5454,0x5455,
    0x5500,0x5501,0x5504,0x5505,0x5510,0x5511,0x5514,0x5515,0x5540,0x5541,0x5544,0x5545,0x5550,0x5551,0x5554,0x5555
};
static const U32 OneBitsData[256]={
    0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,
    1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
    1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
    2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
    1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
    2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
    2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
    3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
    1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
    2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
    2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
    3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
    2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
    3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
    3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
    4,5,5,6,5,6,6,7,5,6,6,7,6,7,7,8
};

static U32 OneBits(const U32 a){  const U08*i=(U08*)&a;  return OneBitsData[i[3]]+OneBitsData[i[2]]+OneBitsData[i[1]]+OneBitsData[i[0]];}
static void BitXor(U32*arr,const U32 index){  arr[index>>5]^=  1<<(index&0x1F) ;}
static void BitOne(U32*arr,const U32 index){  arr[index>>5]|=  1<<(index&0x1F) ;}
static void BitOff(U32*arr,const U32 index){  arr[index>>5]&=~(1<<(index&0x1F));}
static  U32 BitGet(const U32*arr,const U32 index){  return 0x1&(arr[index>>5]>>(index&0x1F));}


static U32 SqrtU32(U32 a,U32 bit){
    a<<=bit&1;  bit&=~1;
    U32 c,b=1<<30;  while(b>a)b>>=2;  c=b;  a-=b;
    while((b>>=2)!=0)if(a<c+b){  c>>=1;}else{  a-=c+b;  c=(c>>1)+b;}  c<<=bit;  a<<=bit;  b=1<<bit;
    while((b>>=2)!=0)if(a<c+b){  c>>=1;}else{  a-=c+b;  c=(c>>1)+b;}
    return c+(c<a?1:0);
}

static U32 ShiftI32(const I32 a,const I08 b){  return (b>0?a<<b:a>>-b);}
static I32 AbsI32(const I32 a){  return (a<0?-a:a);}
static F32 AbsF32(F32 a){  ((U32*)&a)[0]&=0x7FFFFFFF;  return a;}
static I32 ClampI32(const I32 b,const I32 c,const I32 a){  return c>b?b:(a>c?a:c);}
static U32 ClampU32(const U32 b,const U32 c,const U32 a){  return c>b?b:(a>c?a:c);}
static F32 ClampF32(const F32 b,const F32 c,const F32 a){  return c>b?b:(a>c?a:c);}
static F32 ClampAboveZero(const F32 a){  return ((a<0?-a:a)>=0.00001?a:(a<0?-0.00001:0.00001));}
static U64 Hash(const U64 hash){
    static const U64 mod=0x00000100000001B3L;
    const U08 *u=((U08*)&hash);
    U64 h=0x14650FB0739D0383L;
    h=(u[0]^h)*mod;  h=(u[1]^h)*mod;  h=(u[2]^h)*mod;  h=(u[3]^h)*mod;
    h=(u[4]^h)*mod;  h=(u[5]^h)*mod;  h=(u[6]^h)*mod;  h=(u[7]^h)*mod;
    return h;
}
static U64 Random(U64*hash,const U08 bit){
    static const U64 mod=0x00000100000001B3L;
    const U08 *u=((U08*)&hash[0]);
    U64 h=0x14650FB0739D0383L;
    h=(u[0]^h)*mod;  h=(u[1]^h)*mod;  h=(u[2]^h)*mod;  h=(u[3]^h)*mod;
    h=(u[4]^h)*mod;  h=(u[5]^h)*mod;  h=(u[6]^h)*mod;  h=(u[7]^h)*mod;
    return (((U64*)&hash[0])[0]=h)>>(64-bit);
}

static P64*P64Init(P64*a){  a->x=0L,a->y=0L,a->z=0L;  return a;}


static U32*ShiftU128(I128 a,I08 bit){
    U32*u=(U32*)&a[0];
    while(bit> 31)bit-=32,u[3]=u[2],u[2]=u[1],u[1]=u[0],u[0]=0;
    while(bit<-31)bit+=32,u[0]=u[1],u[1]=u[2],u[2]=u[3],u[3]=0;
    const I08 tib=32-(bit<0?-bit:bit);
    if(bit>0){  u[3]=(u[3]<< bit)|(u[2]>>tib);  u[2]=(u[2]<< bit)|(u[1]>>tib);  u[1]=(u[1]<< bit)|(u[0]>>tib);  u[0]<<= bit;}
    if(bit<0){  u[0]=(u[0]>>-bit)|(u[1]<<tib);  u[1]=(u[1]>>-bit)|(u[2]<<tib);  u[2]=(u[2]>>-bit)|(u[3]<<tib);  u[3]>>=-bit;}
    return a;
}
static U32*ShiftI128(I128 a,I08 bit){
    I32*i=(I32*)&a[0];
    U32*u=(U32*)&a[0];
    while(bit> 31)bit-=32,u[3]=u[2],u[2]=u[1],u[1]=u[0],u[0]=0;
    while(bit<-31)bit+=32,u[0]=u[1],u[1]=u[2],u[2]=u[3],i[3]=(i[3]<0?-1:0);
    const I08 tib=32-(bit<0?-bit:bit);
    if(bit>0){  u[3]=(u[3]<< bit)|(u[2]>>tib);  u[2]=(u[2]<< bit)|(u[1]>>tib);  u[1]=(u[1]<< bit)|(u[0]>>tib);  u[0]<<= bit;}
    if(bit<0){  u[0]=(u[0]>>-bit)|(u[1]<<tib);  u[1]=(u[1]>>-bit)|(u[2]<<tib);  u[2]=(u[2]>>-bit)|(u[3]<<tib);  i[3]>>=-bit;}
    return a;
}
static U32*SetI128(I128 b,const I128 a){
    ((U64*)&b[0])[1]=((U64*)&a[0])[1];
    ((U64*)&b[0])[0]=((U64*)&a[0])[0];
    return b;
}
static U32*SetI128U64(I128 b,const U64 a){
    ((U64*)&b[0])[1]=0L;
    ((U64*)&b[0])[0]=a;
    return b;
}
static U32*AddI128(I128 b,const I128 a){
    U64 carry;
    carry=( (U64)      b[0])+a[0];   b[0]=((U32*)&carry)[0];
    carry=((carry>>32)+b[1])+a[1];   b[1]=((U32*)&carry)[0];
    carry=((carry>>32)+b[2])+a[2];   b[2]=((U32*)&carry)[0];
    carry=((carry>>32)+b[3])+a[3];   b[3]=((U32*)&carry)[0];
    return b;
}
static U32*SubI128(I128 b,const I128 a){
    U64 carry;
    carry=        1L +((U64)b[0])+~a[0];   b[0]=((U32*)&carry)[0];
    carry=(carry>>32)+((U64)b[1])+~a[1];   b[1]=((U32*)&carry)[0];
    carry=(carry>>32)+((U64)b[2])+~a[2];   b[2]=((U32*)&carry)[0];
    carry=(carry>>32)+((U64)b[3])+~a[3];   b[3]=((U32*)&carry)[0];
    return b;
}
static U32*AndI128(I128 b,const I128 a){  b[3]&=a[3],b[2]&=a[2],b[1]&=a[1],b[0]&=a[0];  return b;}
static U32*XorI128(I128 b,const I128 a){  b[3]^=a[3],b[2]^=a[2],b[1]^=a[1],b[0]^=a[0];  return b;}
static U32* OrI128(I128 b,const I128 a){  b[3]|=a[3],b[2]|=a[2],b[1]|=a[1],b[0]|=a[0];  return b;}
static U32*AddI128One(I128 a){  if(0==++a[0]&&0==++a[1]&&0==++a[2])++a[3];  return a;}
static U32*SubI128One(I128 a){  if(0==a[0]--&&0==a[1]--&&0==a[2]--)--a[3];  return a;}
static U32*InvI128(I128 a){  a[3]=~a[3],a[2]=~a[2],a[1]=~a[1],a[0]=~a[0];  return a;}
static U32*NegI128(I128 a){  a[3]=~a[3],a[2]=~a[2],a[1]=~a[1],a[0]=~a[0];  if(0==++a[0]&&0==++a[1]&&0==++a[2])++a[3];  return a;}
static U32*AbsI128(I128 a){  return ((I32*)&a[3])[0]<0?NegI128(a):a;}
static U32*ZeroI128(I128 a){  a[3]=0,a[2]=0,a[1]=0,a[0]=0;  return a;}
static U32 NegativeI128(const I128 a){  return (a[3]>>31)&0x1;}
static U32 NotI128(const I128 a){  return a[3]==0&&a[2]==0&&a[1]==0&&a[0]==0;}
static U32 EqualsU128(const I128 b,const I128 a){  return b[3]==a[3]&&b[2]==a[2]&&b[1]==a[1]&&b[0]==a[0];}
static U32 LessThanU128(const I128 b,const I128 a){  if(b[3]!=a[3])return b[3]<a[3];  if(b[2]!=a[2])return b[2]<a[2];  if(b[1]!=a[1])return b[1]<a[1];  return b[0]<a[0];}
static U32 GreaterThanU128(const I128 b,const I128 a){  if(b[3]!=a[3])return b[3]>a[3];  if(b[2]!=a[2])return b[2]>a[2];  if(b[1]!=a[1])return b[1]>a[1];  return b[0]>a[0];}
static U32 LessThanEqualToU128(const I128 b,const I128 a){  if(b[3]!=a[3])return b[3]<a[3];  if(b[2]!=a[2])return b[2]<a[2];  if(b[1]!=a[1])return b[1]<a[1];  return b[0]<=a[0];}
static U32 GreaterThanEqualToU128(const I128 b,const I128 a){  if(b[3]!=a[3])return b[3]>a[3];  if(b[2]!=a[2])return b[2]>a[2];  if(b[1]!=a[1])return b[1]>a[1];  return b[0]>=a[0];}

static U32*MulU128(I128 b,const I128 a,U08 fix){
    const U64 uB[4]={b[0],b[1],b[2],b[3]};
    const U64 uA[4]={a[0],a[1],a[2],a[3]};
    U32 c[8]={0,0,0,0,0,0,0,0};
    ((U64*)&c[0])[0] =                                          (uB[0]*uA[0]);
    ((U64*)&c[1])[0]+=                            (uB[1]*uA[0])+(uB[0]*uA[1]);
    ((U64*)&c[2])[0]+=              (uB[2]*uA[0])+(uB[1]*uA[1])+(uB[0]*uA[2]);
    ((U64*)&c[3])[0]+=(uB[3]*uA[0])+(uB[2]*uA[1])+(uB[1]*uA[2])+(uB[0]*uA[3]);
    ((U64*)&c[4])[0]+=(uB[3]*uA[1])+(uB[2]*uA[2])+(uB[1]*uA[3])              ;
    ((U64*)&c[5])[0]+=(uB[3]*uA[2])+(uB[2]*uA[3])                            ;
    ((U64*)&c[6])[0]+=(uB[3]*uA[3])                                          ;
    const U32 i=fix>>5;
    const U32 bit=(fix&0x1F);
    if(bit){
        b[3]=(c[i+4]<<(32-bit))|(c[i+3]>>bit);
        b[2]=(c[i+3]<<(32-bit))|(c[i+2]>>bit);
        b[1]=(c[i+2]<<(32-bit))|(c[i+1]>>bit);
        b[0]=(c[i+1]<<(32-bit))|(c[i+0]>>bit);
    }else{  b[3]=c[i+3],b[2]=c[i+2],b[1]=c[i+1],b[0]=c[i+0];}
    return b;
}
static U32*MulI128(I128 b,const I128 a,U08 fix){
    U32 neg=0x0;
    I128 c;  SetI128(c,a);
    if(NegativeI128(b)){  neg^=0x1;  NegI128(b);}
    if(NegativeI128(c)){  neg^=0x1;  NegI128(c);}
    MulU128(b,c,fix);
    if(neg)NegI128(b);
    return b;
}

static F32 LookupBit_F32B[128];
static F32 LookupBit_F32A[128];
static F32 Bit_F32(I08 fix){
    static const F32 mod=     (F32)(1L<<32);
    static const F32 inv=1.0f/(F32)(1L<<32);
    F32 f=1.0f;
    while(fix> 31){  fix-=32;  f*=mod;}
    while(fix<-31){  fix+=32;  f*=inv;}
    return (fix<0?f/(1L<<-fix):f*(1L<<fix));
}
static F32 I128_F32(I128 a,const U08 fix){
    F32 f;
    if(NegativeI128(a)){
        NegI128(a);
        f=-((LookupBit_F32B[fix]*((U64*)&a[0])[1])+(LookupBit_F32A[fix]*((U64*)&a[0])[0]));
        NegI128(a);
    }else{  f=(LookupBit_F32B[fix]*((U64*)&a[0])[1])+(LookupBit_F32A[fix]*((U64*)&a[0])[0]);}
    return f;
}
static F32 SubI128_F32Fast(const I128 b,const I128 a){
    static const F32 inv=(1.0f/(F32)(1L<<32))*(1.0f/(F32)(1L<<32));
    F32 f;
    U64 carry;
    I128 c;
    carry=        1L +b[0]+(~a[0]);   c[0]=((U32*)&carry)[0];
    carry=(carry>>32)+b[1]+(~a[1]);   c[1]=((U32*)&carry)[0];
    carry=(carry>>32)+b[2]+(~a[2]);   c[2]=((U32*)&carry)[0];
    carry=(carry>>32)+b[3]+(~a[3]);   c[3]=((U32*)&carry)[0];
    if(((I32*)&c[3])[0]<0){
            c[0]=~c[0],c[1]=~c[1],c[2]=~c[2],c[3]=~c[3];
            if(0==++c[0]&&0==++c[1]&&0==++c[2])++c[3];
            f=-(((F32)((U64*)&c[0])[1])+(inv*(F32)((U64*)&c[0])[0]));
    }else{  f=  ((F32)((U64*)&c[0])[1])+(inv*(F32)((U64*)&c[0])[0]);}
    return f;
}

static F32 I128_F32Fast(const I128 a){
    static const F32 inv=(1.0f/(F32)(1L<<32))*(1.0f/(F32)(1L<<32));
    F32 f;
    if(((I32*)&a[3])[0]<0){
            I128 c={a[0],a[1],a[2],a[3]};
            c[0]=~c[0],c[1]=~c[1],c[2]=~c[2],c[3]=~c[3];  if(0==++c[0]&&0==++c[1]&&0==++c[2])++c[3];
            f=-(((F32)((U64*)&c[0])[1])+(inv*(F32)((U64*)&c[0])[0]));
    }else{  f=  ((F32)((U64*)&a[0])[1])+(inv*(F32)((U64*)&a[0])[0]);}
    return f;
}
static P128*ShiftP128(P128*a,const I08 shift){  ShiftI128(a->x,shift);  ShiftI128(a->y,shift);  ShiftI128(a->z,shift);  return a;}
static P128*AddP128(P128*b,const P128*a){  AddI128(b->x,a->x);  AddI128(b->y,a->y);  AddI128(b->z,a->z);  return b;}
static P128*SubP128(P128*b,const P128*a){  SubI128(b->x,a->x);  SubI128(b->y,a->y);  SubI128(b->z,a->z);  return b;}
static P128*SetP128(P128*b,const P128*a){  SetI128(b->x,a->x);  SetI128(b->y,a->y);  SetI128(b->z,a->z);  return b;}
static P128*NegP128(P128*a){  NegI128(a->x);  NegI128(a->y);  NegI128(a->z);  return a;}
static P128*ZeroP128(P128*a){  ZeroI128(a->x);  ZeroI128(a->y);  ZeroI128(a->z);  return a;}
static P128*MulP128(P128*b,const P128*a,const U08 fix){  MulI128(b->x,a->x,fix);  MulI128(b->y,a->y,fix);  MulI128(b->z,a->z,fix);  return b;}
static P128*MulP128_I128(P128*r,const I128 a,const U08 fix){  MulI128(r->x,a,fix);  MulI128(r->y,a,fix);  MulI128(r->z,a,fix);  return r;}


static T128*SetT128(T128*r,const T128*a){  SetP128(&r->p,&a->p);  SetP128(&r->v,&a->v);  SetP128(&r->a,&a->a);  r->millis=a->millis;  return r;}
static T128*MulT128(T128*r,I32 millis){
    millis-=r->millis;
    static const U08 fix=10;
    P128 a;
    I128 m={millis,millis>>31,millis>>31,millis>>31};
    AddP128(&r->p,MulP128_I128(SetP128(&a,&r->v),m,fix));
    AddP128(&r->v,MulP128_I128(SetP128(&a,&r->a),m,fix));
    m[0]=((1<<fix)+(millis*AbsI32(millis)))>>(fix+1);  m[1]=((I32*)m)[0]>>31;  m[1]=((I32*)m)[0]>>31;  m[1]=((I32*)m)[0]>>31;
    AddP128(&r->p,MulP128_I128(SetP128(&a,&r->a),m,fix));
    r->millis+=millis;
    return r;
}






static void V32Cross(const V32*a,const V32*b,V32*r){  r->x=(a->y*b->z)-(a->z*b->y),r->y=(a->z*b->x)-(a->x*b->z),r->z=(a->x*b->y)-(a->y*b->x);}
static void V32SurfaceNormal(const V32*a,const V32*b,const V32*c,V32*r){
    V32 vA,vB;
    vA.x=b->x-a->x;  vB.x=c->x-a->x;
    vA.y=b->y-a->y;  vB.y=c->y-a->y;
    vA.z=b->z-a->z;  vB.z=c->z-a->z;
    V32Cross(&vA,&vB,r);
    *r=normalize(*r);
}
static U32 V32Scale(V32*v,const F32 scale){
    if(v->z==0&&v->y==0&&v->x==0){  v->z=scale;  return 2;}
    const F32 z=(v->z<0)?-v->z:v->z;
    const F32 y=(v->y<0)?-v->y:v->y;
    const F32 x=(v->x<0)?-v->x:v->x;
    const U32 i=(x>y?((x>z)?0:2):((y>z)?1:2));
    if(i==2){  v->z=v->z<0?-scale:scale;  v->y*=scale/z;  v->x*=scale/z;}else
    if(i==1){  v->y=v->y<0?-scale:scale;  v->x*=scale/y;  v->z*=scale/y;}else
    if(i==0){  v->x=v->x<0?-scale:scale;  v->y*=scale/x;  v->z*=scale/x;}
    return i;
}

static void P64Set(P64*b,const P64*a){  b->x=a->x;  b->y=a->y;  b->z=a->z;}

static I08 SP2Y(const U32 index){  return (index>>1)&1;}
static I08 SP2X(const U32 index){  return (index>>0)&1;}
static U08 SP2N(const U32 x,const U32 y,const U08 bit){  return (((y>>bit)&1)<<1)|(((x>>bit)&1)<<0);}
static U32 SP2Weave(const U32 x,const U32 y){
    return (WeaveFF[((U08*)&y)[1]]<<17)|(WeaveFF[((U08*)&x)[1]]<<16)|(WeaveFF[((U08*)&y)[0]]<<1)|(WeaveFF[((U08*)&x)[0]]<<0);
}

static I08 SP3GetX(const I32 index){  return  (index% 3)    -1;}
static I08 SP3GetY(const I32 index){  return ((index% 9)/ 3)-1;}
static I08 SP3GetZ(const I32 index){  return ((index%27)/ 9)-1;}
static U08 SP3GetD(const U32 index){  return  (index    /27)  ;}
static U08 SP3GetN(const P128*pos,const U08 bit){
    const U08 i=bit>>5,s=bit&0x1F;
    const U08 z=((pos->z[i]>>s)&1)<<2;
    const U08 y=((pos->y[i]>>s)&1)<<1;
    const U08 x= (pos->x[i]>>s)&1    ;
    const U08 n=z|y|x;
    return n;
}
static U32 SP3GetP(const P128*pos,const U32 index,const U08 bit){
    const U08 i=bit>>5,s=bit&0x1F;
    const U08 zA=((pos->z[i]^T128Camera.p.z[i])>>s)&1;
    const U08 yA=((pos->y[i]^T128Camera.p.y[i])>>s)&1;
    const U08 xA=((pos->x[i]^T128Camera.p.x[i])>>s)&1;
    const U08 zB=9+(9*zA*SP3GetZ(index));
    const U08 yB=3+(3*yA*SP3GetY(index));
    const U08 xB=1+(1*xA*SP3GetX(index));
    return zB+yB+xB;
}
static void SP3RunOffset(P128*offset,const U32 index,const U08 lastBit){
    const I64 x=SP3GetX(index);
    const I64 y=SP3GetY(index);
    const I64 z=SP3GetZ(index);
    U64*X=(U64*)&(offset->x[0]);
    U64*Y=(U64*)&(offset->y[0]);
    U64*Z=(U64*)&(offset->z[0]);
    if(lastBit>63){
       X[1]=x<<(lastBit&63);  X[0]=0L;
       Y[1]=y<<(lastBit&63);  Y[0]=0L;
       Z[1]=z<<(lastBit&63);  Z[0]=0L;
    }else{
        X[1]=x<0L?-1L:0L;  X[0]=x<<lastBit;
        Y[1]=y<0L?-1L:0L;  Y[0]=y<<lastBit;
        Z[1]=z<0L?-1L:0L;  Z[0]=z<<lastBit;
    }
    //ShiftP128(offset,stopBit);
}
static void SP3Hash(P64*cSeed,const U08 n){
    U64 s=Hash((U64)n);
    cSeed->x=Hash(cSeed->x^(s<<=8));
    cSeed->y=Hash(cSeed->y^(s<<=8));
    cSeed->z=Hash(cSeed->z^(s<<=8));
}
static void SP3Run(P64*cSeed,const P128*pos,const U08 startBit,const U08 stopBit){
    for(U08 bit=startBit;bit!=stopBit;--bit)SP3Hash(cSeed,SP3GetN(pos,bit));
}

static const U32 FontHex[16]={
    0x69996,0x22227,0x6924F,0xE161E,
    0x99F11,0xF8E16,0x68E96,0xF1244,
    0x69696,0x79711,0x69F99,0xE9E9E,
    0x6A896,0xE999E,0x78E87,0xF8E88
};
typedef struct{  U32 I,J,T,S[256];}Arc4;
static void Arc4Set(Arc4*arc4,const U64 key){
    U32 bits=64,i=0;
    for(arc4->I=0;arc4->I!=256;++arc4->I)arc4->S[arc4->I]=arc4->I;
    for(arc4->I=0,arc4->J=0;arc4->I!=256;++arc4->I){
        arc4->J=255&(arc4->J+arc4->S[arc4->I]+(1&(key>>arc4->I)));
        arc4->T=arc4->S[arc4->I];  arc4->S[arc4->I]=arc4->S[arc4->J];  arc4->S[arc4->J]=arc4->T;
        if(++i==bits)i=0;
    }
    arc4->I=0;  arc4->J=0;
}
static U32 Cypher08(Arc4*arc4){
    arc4->J=255&(arc4->J+arc4->S[arc4->I=255&(arc4->I+1)]);
    arc4->T=arc4->S[arc4->I];
    arc4->S[arc4->I]=arc4->S[arc4->J];
    arc4->S[arc4->J]=arc4->T;
    return arc4->S[(arc4->S[arc4->I]+arc4->S[arc4->J])&255];
}
static U32 Cypher16(Arc4*arc4){  return Cypher08(arc4)|(Cypher08(arc4)<<8);}
static U32 Cypher24(Arc4*arc4){  return Cypher08(arc4)|(Cypher08(arc4)<<8)|(Cypher08(arc4)<<16);}
static U32 Cypher32(Arc4*arc4){  return Cypher08(arc4)|(Cypher08(arc4)<<8)|(Cypher08(arc4)<<16)|(Cypher08(arc4)<<24);}
static U64 Cypher64(Arc4*arc4){  return Cypher32(arc4)|(((U64)Cypher32(arc4))<<32);}

static I32 RandomI32(U64*hash,const U08 bits){  return (I32)Random(hash,bits);}
static Arc4 Arc4A;
static U64 GalaxySeed[GalaxyStarEnd];
static P128 GalaxyP128A[GalaxyStarEnd];
static U08 GalaxyColorIndex[GalaxyEnd];
static F32 GalaxyColorAlpha[GalaxyEnd];
static F32 GalaxyColorSize[GalaxyEnd];
static U32 StarColor[8]={0x00FFB09B,0x00FFB09B,0x00FFBFAA,0x00FFD7CA,0x00FFF7F8,0x00EAF4FF,0x00A1D2FF,0x006FCCFF};
static void GalaxySetColor(const U32 index,const F32 density){
    GalaxyColorIndex[index]=Cypher08(&Arc4A)>>5;
    GalaxyColorAlpha[index]=ClampI32(0xFF,round(0xFF*density),0x08)/255.0;
    GalaxyColorSize[index] =Cypher08(&Arc4A)/255.0;
}
static F32 GalaxyGetDensity(F32 range,const F32 angle){
    const F32 near=range*0.25f;
    range-=0.5f*(I32)(2*range);
    range-=angle-(0.5f*(I32)(2*angle)); ((U32*)&range)[0]&=0x7FFFFFFF;
    range-=0.5f*(I32)(4*range);         ((U32*)&range)[0]&=0x7FFFFFFF;
    range=1.0f-(range>near?near:range);
    return range*range*range;
}

static F32 Atan2Unit(const F32 y,const F32 x){  const F32 a=0.5f+(PI2Inv*atan2(y,x));  return a-(I32)a;}
static F32 GalaxyGetDensityV32(const V32*v){
    if(1.0f<(v->x*v->x)+(v->y*v->y)+(v->z*v->z))return 0;
    const F32 range=sqrt((v->x*v->x)+(v->y*v->y)+(v->z*v->z));  //3D range intended//
    const F32 angle=Atan2Unit(v->y,v->x);
    const F32 z=1.0f-AbsF32(v->z);
    const F32 r=1.0f-range;
    return (r*r)*(z*z)*GalaxyGetDensity(range,angle);
}



static U32*DivU128(I128 result,const I128 divisor,I128 remain){
	if(NotI128(divisor))return result;
	I128 part;  SetI128(part,divisor);  SetI128(remain,result);
	result[3]=0,result[2]=0,result[1]=0,result[0]=0;
	I128 mask={1,0,0,0};
	while(LessThanU128(part,remain)){  ShiftU128(part,1);  ShiftU128(mask,1);}
	do{
	    if(GreaterThanEqualToU128(remain,part)){
	        SubI128(remain,part);
	        AddI128(result,mask);
	    }
	    ShiftU128(part,-1);
	    ShiftU128(mask,-1);
	}while(!NotI128(mask));
    return result;
}
static U32 BitCount(U32 a){
    int b=0;
    if((a>>16))b|=16,a>>=16;
    if((a>> 8))b|= 8,a>>= 8;
    if((a>> 4))b|= 4,a>>= 4;
    if((a>> 2))b|= 2,a>>= 2;
    if((a>> 1))b|= 1,a>>= 1;
    if(a)++b;
    return b;
}
static U32 BitCountI128(I128 a){
    int b=0;
    if(a[3]){  b=96+BitCount(a[3]);}else
    if(a[2]){  b=64+BitCount(a[2]);}else
    if(a[1]){  b=32+BitCount(a[1]);}else
    if(a[0]){  b=   BitCount(a[0]);}
    return b;
}

static U32*DivI128(I128 a,const I128 D,const U08 fix){
    if(NotI128(D))return a;
    U32 neg=0x0;
    I128 r,c,d={D[0],D[1],D[2],D[3]};
    if(NegativeI128(a)){  neg^=0x1;  NegI128(a);}
    if(NegativeI128(d)){  neg^=0x1;  NegI128(d);}
    DivU128(a,d,r);  ShiftU128(a,fix);
    const I32 x=BitCountI128(r);
    const I32 y=BitCountI128(d);
    ShiftI128(r,126-x);
    ShiftI128(d,((126-fix)+(y-x))-y);
    DivU128(r,d,c);
    OrI128(a,r);
    if(neg)NegI128(a);
    return a;
}
static const I32 FixBit=62;
static const I128 FixUnit={0x00000000,0x40000000,0x00000000,0x00000000};
static const I128 FixHalf={0x00000000,0x20000000,0x00000000,0x00000000};
static const I128 FixFourth={0x00000000,0x10000000,0x00000000,0x00000000};
static const I128 FixZero={0x00000000,0x00000000,0x00000000,0x00000000};
static const I128 FixPI  ={0xFFFFFFFF,0x3FFFFFFF,0x00000000,0x00000000};
static const I128 FixPI2 ={0xFFFFFFFF,0x7FFFFFFF,0x00000000,0x00000000};
static const I128 FixSin ={0x66666666,0x0E666666,0x00000000,0x00000000};//(225/1000)*(2^60)
static const P128 FixAxisX={{0,1<<30,0,0},{0,0,0,0},{0,0,0,0}};
static const P128 FixAxisY={{0,0,0,0},{0,1<<30,0,0},{0,0,0,0}};
static const P128 FixAxisZ={{0,0,0,0},{0,0,0,0},{0,1<<30,0,0}};
static U32*SinI128(I128 a){
    I128 c,b;
    AddI128(a,FixUnit);  AndI128(a,FixPI2);  SubI128(a,FixUnit);//long a=((angle+Unit)&MaskPI2)-Unit;
    SetI128(c,a);  SetI128(b,a);  AbsI128(b);  MulI128(c,b,FixBit);  SubI128(a,c);  ShiftI128(a,2);//a=Shf(a,2)-Shf(Mul(a,Hack.ABS(a)),2);
    SetI128(c,a);  SetI128(b,a);  NegI128(c);  AbsI128(b);
    MulI128(b,a,FixBit);  AddI128(c,b);  MulI128(c,FixSin,FixBit);  AddI128(a,c);//a+=Mul(SinA,-a+Mul(a,Hack.ABS(a)));
    return a;
}
static U32*CosI128(I128 angle){  return SinI128(AddI128(angle,FixHalf));}
static U32*SqrtU128(I128 a,U08 fix){
    ShiftU128(a,fix&1);  fix&=~1;
    I128 d,c,b={0,0,0,1<<30};
    while(GreaterThanU128(b,a))ShiftU128(b,-2);  SetI128(c,b);  SubI128(a,b);
    while(!NotI128(ShiftU128(b,-2))){
        AddI128(SetI128(d,c),b);
        if(LessThanU128(a,d)){  ShiftU128(c,-1);}else{  SubI128(a,d);  AddI128(ShiftU128(c,-1),b);}
    }
    ShiftU128(c,fix);  ShiftU128(a,fix);  b[3]=0,b[2]=0,b[1]=0,b[0]=1;  ShiftU128(b,fix);
    while(!NotI128(ShiftU128(b,-2))){
        AddI128(SetI128(d,c),b);
        if(LessThanU128(a,d)){  ShiftU128(c,-1);}else{  SubI128(a,d);  AddI128(ShiftU128(c,-1),b);}
    }
    if(LessThanU128(c,a))AddI128One(c);
    return SetI128(a,c);
}
static void SqrtU128Test(I128 d,I128 c,I128 b,I128 a){
    while(!NotI128(ShiftU128(b,-2))){
        AddI128(SetI128(d,c),b);
        if(LessThanU128(a,d)){  ShiftU128(c,-1);}else{  SubI128(a,d);  AddI128(ShiftU128(c,-1),b);}
    }
}
static U32*SqrtU128Prec(I128 a,U08 fix){
    I128 d,c,b={0,0,0,1<<30};
    ShiftU128(a,fix&1);  fix&=~1;
    while(GreaterThanU128(b,a))ShiftU128(b,-2);  SetI128(c,b);  SubI128(a,b);
    SqrtU128Test(d,c,b,a);
    while(fix>31){
        fix-=32;
        c[3]=c[2],c[2]=c[1],c[1]=c[0],c[0]=0;
        b[3]=b[2],b[2]=b[1],b[1]=b[0],b[0]=0;
        b[3]=  0 ,b[2]=  0 ,b[1]=  1 ,b[0]=0;
        SqrtU128Test(d,c,b,a);
    }
    if(fix){
        ShiftU128(c,fix);
        ShiftU128(b,fix);  b[3]=0,b[2]=0,b[1]=0,b[0]=1;
        ShiftU128(b,fix);
        SqrtU128Test(d,c,b,a);
    }

    if(LessThanU128(c,a))AddI128One(c);
    return SetI128(a,c);
}

static U32*MagQ128(const Q128*q,I128 mag){
    I128 mul;  ZeroI128(mag);
    SetI128(mul,q->x);  AddI128(mag,MulI128(mul,q->x,FixBit));
    SetI128(mul,q->y);  AddI128(mag,MulI128(mul,q->y,FixBit));
    SetI128(mul,q->z);  AddI128(mag,MulI128(mul,q->z,FixBit));
    SetI128(mul,q->w);  AddI128(mag,MulI128(mul,q->w,FixBit));
    return mag;
}
static void NormQ128(Q128*q){
    I128 mag,inv;
    MagQ128(q,mag);  SqrtU128(mag,FixBit);
    SetI128(inv,FixUnit);  DivI128(inv,mag,FixBit);
    MulI128(q->x,inv,FixBit);
    MulI128(q->y,inv,FixBit);
    MulI128(q->z,inv,FixBit);
    MulI128(q->w,inv,FixBit);
}
static void SetQ128(Q128*q,const P128*axis,const I128 angle){
    CosI128(ShiftI128(SetI128(q->w,angle),-1));
    SinI128(ShiftI128(SetI128(q->x,angle),-1));  SetI128(q->y,q->x);  SetI128(q->z,q->x);
	MulI128(q->x,axis->x,FixBit);
	MulI128(q->y,axis->y,FixBit);
	MulI128(q->z,axis->z,FixBit);
	NormQ128(q);
}
static void MulQ128(const Q128*q,P128*p){
    I128 a,b,c,x,y,z;

    SetI128(b,q->y);  SetI128(a,q->z);  MulI128(b,p->z,FixBit);  MulI128(a,p->y,FixBit);  SubI128(b,a);  SetI128(x,b);//x=(q->y*p->z)-(q->z*p->y);
    SetI128(b,q->z);  SetI128(a,q->x);  MulI128(b,p->x,FixBit);  MulI128(a,p->z,FixBit);  SubI128(b,a);  SetI128(y,b);//y=(q->z*p->x)-(q->x*p->z);
    SetI128(b,q->x);  SetI128(a,q->y);  MulI128(b,p->y,FixBit);  MulI128(a,p->x,FixBit);  SubI128(b,a);  SetI128(z,b);//z=(q->x*p->y)-(q->y*p->x);

    SetI128(c,q->w);  SetI128(b,q->y);  SetI128(a,q->z);
    MulI128(c,x,FixBit);  MulI128(b,z,FixBit);  MulI128(a,y,FixBit);
    AddI128(c,b);  SubI128(c,a);  ShiftI128(c,1);  AddI128(p->x,c);

    SetI128(c,q->w);  SetI128(b,q->z);  SetI128(a,q->x);
    MulI128(c,y,FixBit);  MulI128(b,x,FixBit);  MulI128(a,z,FixBit);
    AddI128(c,b);  SubI128(c,a);  ShiftI128(c,1);  AddI128(p->y,c);

    SetI128(c,q->w);  SetI128(b,q->x);  SetI128(a,q->y);
    MulI128(c,z,FixBit);  MulI128(b,y,FixBit);  MulI128(a,x,FixBit);
    AddI128(c,b);  SubI128(c,a);  ShiftI128(c,1);  AddI128(p->z,c);
}

static void GenGalaxyTypeB(){
    Arc4Set(&Arc4A,0L);
    static const U32 halfBit=BgnBitG-1;
    static const U32 armBit=BgnBitG-2;
    static const U32 rangeBit=armBit;
    static const U32 angleBit=FixBit+1;
    static const U32 centerBit=armBit-1;
    I128 rangeMask={1,0,0,0},rangeUnit={1,0,0,0},halfUnit={1,0,0,0};
    I128 armUnit={1,0,0,0},centerUnit={1,0,0,0},centerMask={1,0,0,0};
    SubI128One(ShiftU128(rangeMask,rangeBit));
    ShiftI128(rangeUnit,rangeBit);
    ShiftI128(halfUnit,halfBit);
    ShiftI128(armUnit,armBit);
    ShiftI128(centerUnit,centerBit);
    SubI128One(ShiftI128(centerMask,centerBit));

    I128 angleTemp={3,0,0,0};  ShiftI128(angleTemp,angleBit-3);

    Q128 q128A;
    I128 angle,range,temp,depth;

    for(U32 i=0;i!=GalaxyEnd;++i){
        ZeroP128(&GalaxyP128A[i]);
        GalaxySeed[i]=Cypher64(&Arc4A);
        range[3]=Cypher32(&Arc4A);  angle[3]=Cypher32(&Arc4A);  depth[3]=Cypher32(&Arc4A);
        range[2]=Cypher32(&Arc4A);  angle[2]=Cypher32(&Arc4A);  depth[2]=Cypher32(&Arc4A);
        range[1]=Cypher32(&Arc4A);  angle[1]=Cypher32(&Arc4A);  depth[1]=Cypher32(&Arc4A);
        range[0]=Cypher32(&Arc4A);  angle[0]=Cypher32(&Arc4A);  depth[0]=Cypher32(&Arc4A);
        AndI128(range,rangeMask);
        AndI128(angle,FixPI2);

        SqrtU128Prec(range,rangeBit);
        SqrtU128Prec(range,rangeBit);
        SetI128(temp,rangeUnit);  SubI128(temp,range);  SetI128(range,temp);
        const U08 armIndex=Cypher08(&Arc4A)>>7;
        const U08 armSide=Cypher08(&Arc4A)>>7;
        if(armSide)NegI128(range);
        ShiftI128(AndI128(SetI128(temp,angle),FixPI),armBit-FixBit);
        if(armIndex)AddI128(temp,armUnit);
        AbsI128(AddI128(range,temp));

        if(LessThanU128(range,centerUnit)){
            range[3]=Cypher32(&Arc4A);  angle[3]=Cypher32(&Arc4A);
            range[2]=Cypher32(&Arc4A);  angle[2]=Cypher32(&Arc4A);
            range[1]=Cypher32(&Arc4A);  angle[1]=Cypher32(&Arc4A);
            range[0]=Cypher32(&Arc4A);  angle[0]=Cypher32(&Arc4A);
            SqrtU128Prec(AndI128(range,centerMask),centerBit);
            AndI128(angle,FixPI2);
        }else if(LessThanEqualToU128(halfUnit,range)){
            //AndI128(SetI128(temp,angle),FixPI);
            //if(GreaterThanEqualToU128(temp,angleTemp)){  GalaxyColor[i]=0xFF0000FF;}else{  GalaxyColor[i]=0xFF00FF00;}

            SetI128(temp,  FixPI2);  SubI128(temp,angle);  SetI128(angle,temp);
            SetI128(temp,halfUnit);  SubI128(temp,range);  SetI128(range,temp);  AddI128(range,halfUnit);

        }
        GalaxySetColor(i,GalaxyGetDensity(I128_F32(range,halfBit),I128_F32(angle,angleBit)));

        const U08 mark=depth[0];
        ShiftU128(depth,-8);
        SetI128(GalaxyP128A[i].x,range);  SetQ128(&q128A,&FixAxisZ,angle);  MulQ128(&q128A,&GalaxyP128A[i]);
        AbsI128(SubI128(range,halfUnit));
        ShiftU128(range,-2);
        SqrtU128Prec(range,BgnBitG-4);
        DivU128(depth,range,GalaxyP128A[i].z);
        if(mark<128)NegI128(GalaxyP128A[i].z);

        range[3]=Cypher32(&Arc4A);  range[2]=Cypher32(&Arc4A);  range[1]=Cypher32(&Arc4A);  range[0]=Cypher32(&Arc4A);
        SetI128(angle,rangeMask);  ShiftI128(angle,-1);  AndI128(range,rangeMask);  SubI128(range,angle);  ShiftI128(range,-5);
        AddI128(GalaxyP128A[i].x,range);
        range[3]=Cypher32(&Arc4A);  range[2]=Cypher32(&Arc4A);  range[1]=Cypher32(&Arc4A);  range[0]=Cypher32(&Arc4A);
        SetI128(angle,rangeMask);  ShiftI128(angle,-1);  AndI128(range,rangeMask);  SubI128(range,angle);  ShiftI128(range,-5);
        AddI128(GalaxyP128A[i].y,range);
        range[3]=Cypher32(&Arc4A);  range[2]=Cypher32(&Arc4A);  range[1]=Cypher32(&Arc4A);  range[0]=Cypher32(&Arc4A);
        SetI128(angle,rangeMask);  ShiftI128(angle,-1);  AndI128(range,rangeMask);  SubI128(range,angle);  ShiftI128(range,-5);
        AddI128(GalaxyP128A[i].z,range);
    }
}
static const ColorTableAlpha[256]={
    0x00000000,0x01010101,0x02020202,0x03030303,0x04040404,0x05050505,0x06060606,0x07070707,0x08080808,0x09090909,0x0A0A0A0A,0x0B0B0B0B,0x0C0C0C0C,0x0D0D0D0D,0x0E0E0E0E,0x0F0F0F0F,
    0x10101010,0x11111111,0x12121212,0x13131313,0x14141414,0x15151515,0x16161616,0x17171717,0x18181818,0x19191919,0x1A1A1A1A,0x1B1B1B1B,0x1C1C1C1C,0x1D1D1D1D,0x1E1E1E1E,0x1F1F1F1F,
    0x20202020,0x21212121,0x22222222,0x23232323,0x24242424,0x25252525,0x26262626,0x27272727,0x28282828,0x29292929,0x2A2A2A2A,0x2B2B2B2B,0x2C2C2C2C,0x2D2D2D2D,0x2E2E2E2E,0x2F2F2F2F,
    0x30303030,0x31313131,0x32323232,0x33333333,0x34343434,0x35353535,0x36363636,0x37373737,0x38383838,0x39393939,0x3A3A3A3A,0x3B3B3B3B,0x3C3C3C3C,0x3D3D3D3D,0x3E3E3E3E,0x3F3F3F3F,
    0x40404040,0x41414141,0x42424242,0x43434343,0x44444444,0x45454545,0x46464646,0x47474747,0x48484848,0x49494949,0x4A4A4A4A,0x4B4B4B4B,0x4C4C4C4C,0x4D4D4D4D,0x4E4E4E4E,0x4F4F4F4F,
    0x50505050,0x51515151,0x52525252,0x53535353,0x54545454,0x55555555,0x56565656,0x57575757,0x58585858,0x59595959,0x5A5A5A5A,0x5B5B5B5B,0x5C5C5C5C,0x5D5D5D5D,0x5E5E5E5E,0x5F5F5F5F,
    0x60606060,0x61616161,0x62626262,0x63636363,0x64646464,0x65656565,0x66666666,0x67676767,0x68686868,0x69696969,0x6A6A6A6A,0x6B6B6B6B,0x6C6C6C6C,0x6D6D6D6D,0x6E6E6E6E,0x6F6F6F6F,
    0x70707070,0x71717171,0x72727272,0x73737373,0x74747474,0x75757575,0x76767676,0x77777777,0x78787878,0x79797979,0x7A7A7A7A,0x7B7B7B7B,0x7C7C7C7C,0x7D7D7D7D,0x7E7E7E7E,0x7F7F7F7F,
    0x80808080,0x81818181,0x82828282,0x83838383,0x84848484,0x85858585,0x86868686,0x87878787,0x88888888,0x89898989,0x8A8A8A8A,0x8B8B8B8B,0x8C8C8C8C,0x8D8D8D8D,0x8E8E8E8E,0x8F8F8F8F,
    0x90909090,0x91919191,0x92929292,0x93939393,0x94949494,0x95959595,0x96969696,0x97979797,0x98989898,0x99999999,0x9A9A9A9A,0x9B9B9B9B,0x9C9C9C9C,0x9D9D9D9D,0x9E9E9E9E,0x9F9F9F9F,
    0xA0A0A0A0,0xA1A1A1A1,0xA2A2A2A2,0xA3A3A3A3,0xA4A4A4A4,0xA5A5A5A5,0xA6A6A6A6,0xA7A7A7A7,0xA8A8A8A8,0xA9A9A9A9,0xAAAAAAAA,0xABABABAB,0xACACACAC,0xADADADAD,0xAEAEAEAE,0xAFAFAFAF,
    0xB0B0B0B0,0xB1B1B1B1,0xB2B2B2B2,0xB3B3B3B3,0xB4B4B4B4,0xB5B5B5B5,0xB6B6B6B6,0xB7B7B7B7,0xB8B8B8B8,0xB9B9B9B9,0xBABABABA,0xBBBBBBBB,0xBCBCBCBC,0xBDBDBDBD,0xBEBEBEBE,0xBFBFBFBF,
    0xC0C0C0C0,0xC1C1C1C1,0xC2C2C2C2,0xC3C3C3C3,0xC4C4C4C4,0xC5C5C5C5,0xC6C6C6C6,0xC7C7C7C7,0xC8C8C8C8,0xC9C9C9C9,0xCACACACA,0xCBCBCBCB,0xCCCCCCCC,0xCDCDCDCD,0xCECECECE,0xCFCFCFCF,
    0xD0D0D0D0,0xD1D1D1D1,0xD2D2D2D2,0xD3D3D3D3,0xD4D4D4D4,0xD5D5D5D5,0xD6D6D6D6,0xD7D7D7D7,0xD8D8D8D8,0xD9D9D9D9,0xDADADADA,0xDBDBDBDB,0xDCDCDCDC,0xDDDDDDDD,0xDEDEDEDE,0xDFDFDFDF,
    0xE0E0E0E0,0xE1E1E1E1,0xE2E2E2E2,0xE3E3E3E3,0xE4E4E4E4,0xE5E5E5E5,0xE6E6E6E6,0xE7E7E7E7,0xE8E8E8E8,0xE9E9E9E9,0xEAEAEAEA,0xEBEBEBEB,0xECECECEC,0xEDEDEDED,0xEEEEEEEE,0xEFEFEFEF,
    0xF0F0F0F0,0xF1F1F1F1,0xF2F2F2F2,0xF3F3F3F3,0xF4F4F4F4,0xF5F5F5F5,0xF6F6F6F6,0xF7F7F7F7,0xF8F8F8F8,0xF9F9F9F9,0xFAFAFAFA,0xFBFBFBFB,0xFCFCFCFC,0xFDFDFDFD,0xFEFEFEFE,0xFFFFFFFF
};

void RunFirst(const U32 xD,const U32 yD){
    GenGalaxyTypeB();
    I32 i=0;
    for(i=0;i!=128;++i){  LookupBit_F32B[i]=Bit_F32(64-i);  LookupBit_F32A[i]=Bit_F32(0-i);}
}
void Update(const U32 millis,const U32 warpX,const U32 warpY,const U32 warpZ){
    Millis=millis&0x7FFFFFFF;

    I128 x={warpX&0x00FFFFFF,0,0,0};  ShiftI128(x,((warpX>>24)&0x7F)-24);  SetI128(T128Camera.v.x,x);  if((warpX>>31)!=0)NegI128(T128Camera.v.x);
    I128 y={warpY&0x00FFFFFF,0,0,0};  ShiftI128(y,((warpY>>24)&0x7F)-24);  SetI128(T128Camera.v.y,y);  if((warpY>>31)!=0)NegI128(T128Camera.v.y);
    I128 z={warpZ&0x00FFFFFF,0,0,0};  ShiftI128(z,((warpZ>>24)&0x7F)-24);  SetI128(T128Camera.v.z,z);  if((warpZ>>31)!=0)NegI128(T128Camera.v.z);
    MulT128(&T128Camera,Millis);
    AllocSetF32(allocationV32P,-I128_F32Fast(T128Camera.p.x),0);
    AllocSetF32(allocationV32P,-I128_F32Fast(T128Camera.p.y),1);
    AllocSetF32(allocationV32P,-I128_F32Fast(T128Camera.p.z),2);
    AllocSetF32(allocationV32P,                         0.0f,4);

    P128 pos,offset;
    const U08 stopBit=StartBitStar;
    const U08 lastBit=stopBit+1;
    for(U32 index=0;index!=27;++index){
        SetP128(&pos,&(T128Camera.p));
        SP3RunOffset(&offset,index,lastBit);
        AddP128(&pos,&offset);
        P64Init(&StarSeedA[index]);
        SP3Run(&StarSeedA[index],&pos,MaxBitG,stopBit);
    }
}

void __attribute__((kernel)) Galaxy(const U32 index){
    I32 i=(index*6)-1;
    AllocSetF32(allocationDisk,I128_F32Fast(GalaxyP128A[index].x),++i);
    AllocSetF32(allocationDisk,I128_F32Fast(GalaxyP128A[index].y),++i);
    AllocSetF32(allocationDisk,I128_F32Fast(GalaxyP128A[index].z),++i);
    AllocSetF32(allocationDisk,GalaxyColorIndex[index],++i);
    AllocSetF32(allocationDisk,GalaxyColorAlpha[index],++i);
    AllocSetF32(allocationDisk,0.5f+(0.25f*GalaxyColorSize[index]),++i);
}
static void setAllocStar(const F32 x,const F32 y,const F32 z,const U08 alpha,I32 i){
    static const F32 invert=1.0f/255.0f;
    i=(i*4)-1;
    AllocSetF32(allocationStar,x,++i);
    AllocSetF32(allocationStar,y,++i);
    AllocSetF32(allocationStar,z,++i);
    AllocSetF32(allocationStar,invert*(float)alpha,++i);
}
void __attribute__((kernel)) Star(const U32 index){
    const U08 startBit=StartBitStar;
    const U08 lastBit =StartBitStar-SP3GetD(index);
    const U08 stopBit =lastBit-1;

    P128 pos,offset;

    SetP128(&pos,&(T128Camera.p));
    SP3RunOffset(&offset,index,lastBit);
    AddP128(&pos,&offset);
    P64Set(&StarSeedB[index],&StarSeedA[SP3GetP(&pos,index,startBit+1)]);
    SP3Run(&StarSeedB[index],&pos,startBit,stopBit);
    GalaxySeed[GalaxyEnd|index]=StarSeedB[index].x^StarSeedB[index].y^StarSeedB[index].z;

    const U64 maskB=(lastBit>63?(1L<<(lastBit&63))-1L: 0L);
    const U64 maskA=(lastBit<64?(1L<< lastBit    )-1L:-1L);
    ((U64*)&pos.x[0])[1]&=~maskB;  ((U64*)&pos.x[0])[0]&=~maskA;
    ((U64*)&pos.y[0])[1]&=~maskB;  ((U64*)&pos.y[0])[0]&=~maskA;
    ((U64*)&pos.z[0])[1]&=~maskB;  ((U64*)&pos.z[0])[0]&=~maskA;
    ((U64*)&offset.x[0])[1]=0L;  ((U64*)&offset.x[0])[0]=StarSeedB[index].x;
    ((U64*)&offset.y[0])[1]=0L;  ((U64*)&offset.y[0])[0]=StarSeedB[index].y;
    ((U64*)&offset.z[0])[1]=0L;  ((U64*)&offset.z[0])[0]=StarSeedB[index].z;
    AddP128(&pos,ShiftP128(&offset,lastBit-64));
    SetP128(&GalaxyP128A[GalaxyEnd|index],&pos);
    SubP128(&pos,&T128Camera.p);


    SetP128(&offset,&T128Camera.p);
    ((U64*)&offset.x[0])[1]&=maskB;  ((U64*)&offset.x[0])[0]&=maskA;
    ((U64*)&offset.y[0])[1]&=maskB;  ((U64*)&offset.y[0])[0]&=maskA;
    ((U64*)&offset.z[0])[1]&=maskB;  ((U64*)&offset.z[0])[0]&=maskA;
    ShiftP128(&offset,8-lastBit);

    P08 p08A,p08B;
    p08A.x=((U08*)&offset.x[0])[0];  p08B.x=SP3GetX(index);
    p08A.y=((U08*)&offset.y[0])[0];  p08B.y=SP3GetY(index);
    p08A.z=((U08*)&offset.z[0])[0];  p08B.z=SP3GetZ(index);
    p08A.x=(p08B.x==0?0xFF:(p08B.x==1?p08A.x:~p08A.x));
    p08A.y=(p08B.y==0?0xFF:(p08B.y==1?p08A.y:~p08A.y));  if(p08A.y<p08A.x)p08A.x=p08A.y;
    p08A.z=(p08B.z==0?0xFF:(p08B.z==1?p08A.z:~p08A.z));  if(p08A.z<p08A.x)p08A.x=p08A.z;

    setAllocStar(I128_F32(pos.x,64),I128_F32(pos.y,64),I128_F32(pos.z,64),p08A.x,index);
}