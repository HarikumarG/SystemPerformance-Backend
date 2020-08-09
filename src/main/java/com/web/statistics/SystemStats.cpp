#include<jni.h>
#include "com_web_statistics_Statistics.h"
#include<iostream>
#include <sys/sysinfo.h>
using namespace std;

JNIEXPORT jobject JNICALL Java_com_web_statistics_Statistics_getStats(JNIEnv *env, jobject thisObj)
{
	struct sysinfo si;

	jclass statsclass = env->FindClass("com/web/statisticsmodel/StatisticsModel");
	jobject statsdata = env->AllocObject(statsclass);

	if(sysinfo(&si) != -1)
	{
		jfieldID totalram = env->GetFieldID(statsclass,"totalram","J");
		jfieldID freeram = env->GetFieldID(statsclass,"freeram", "J");
		jfieldID totalswap = env->GetFieldID(statsclass,"totalswap","J");
		jfieldID freeswap = env->GetFieldID(statsclass,"freeswap","J");
		jfieldID loadavgpast1 = env->GetFieldID(statsclass,"loadavgpast1","F");
		jfieldID loadavgpast5 = env->GetFieldID(statsclass,"loadavgpast5","F");
		jfieldID loadavgpast15 = env->GetFieldID(statsclass,"loadavgpast15","F");

		env->SetLongField(statsdata,totalram,si.totalram);
		env->SetLongField(statsdata,freeram,si.freeram);
		env->SetLongField(statsdata,totalswap,si.totalswap);
		env->SetLongField(statsdata,freeswap,si.freeswap);
		env->SetFloatField(statsdata,loadavgpast1,si.loads[0]/65536.0);
		env->SetFloatField(statsdata,loadavgpast5,si.loads[1]/65536.0);
		env->SetFloatField(statsdata,loadavgpast15,si.loads[2]/65536.0);
	}

	return statsdata;

}