#include <bits/stdc++.h>
#include <sys/sysinfo.h>
#include <curl/curl.h>
#include <thread>
#include <time.h>
using namespace std;
//windows wmic csproduct get UUID
//ubuntu sudo dmidecode -s system-uuid
//SystemName - SYSTEM-1 
//UUID - 93C21FDA-BF58-E311-A970-201A0687E7C5
bool connected = true;
time_t rawtime;
char dateChar[11];
char timeChar[9];
struct tm *timeinfo;
string uuid = "93C21FDA-BF58-E311-A970-201A0687E7C5";

void PostJson(string data) {
    CURL *curl = NULL;
    CURLcode res = CURLE_FAILED_INIT;
    char errbuf[CURL_ERROR_SIZE] = { 0, };
    struct curl_slist *headers = NULL;
    char agent[1024] = {0};
    curl = curl_easy_init();
    if(!curl) {
        fprintf(stderr, "Error: curl_easy_init failed.\n");
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
        exit(0);
    }

    string jsonStr = "{\"data\" : \""+data+"\"}";
    const char *jsonData = jsonStr.c_str();

    curl_easy_setopt(curl, CURLOPT_CAINFO, "curl-ca-bundle.crt");
    curl_easy_setopt(curl, CURLOPT_USERAGENT, agent);
    headers = curl_slist_append(headers, "Expect:");
    headers = curl_slist_append(headers, "Content-Type: application/json");
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, jsonData);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDSIZE, -1L);
    curl_easy_setopt(curl, CURLOPT_URL,"http://localhost:8080/SystemPerformance-Backend/storeStats");
    curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L);
    curl_easy_setopt(curl, CURLOPT_ERRORBUFFER, errbuf);

    res = curl_easy_perform(curl);

    if(res != CURLE_OK) {
        size_t len = strlen(errbuf);
        fprintf(stderr, "\nlibcurl: (%d) ", res);
        if(len)
            fprintf(stderr, "%s%s", errbuf, ((errbuf[len - 1] != '\n') ? "\n" : ""));
        fprintf(stderr, "%s\n\n", curl_easy_strerror(res));
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
        exit(0);
    }
}
string getStats() {
    struct sysinfo si;
    if(sysinfo(&si) != -1) {
        time(&rawtime);
        timeinfo = localtime(&rawtime);
        strftime(dateChar,11,"%F",timeinfo);
        strftime(timeChar,9,"%T",timeinfo);
        string date = dateChar;
        string time = timeChar;
        float trf = si.totalram;
        float tr = trf/1000000000;
        string totalram = to_string(tr);
        long uri = si.totalram-si.freeram;
        float urf = uri;
        float ur = urf/1000000000;
        string usedram = to_string(ur);
        int val = round(((si.loads[0]/65536.0)*100)/2);
        string cpuusage = to_string(val);
        string data = uuid+"/"+totalram+"/"+usedram+"/"+cpuusage+"/"+date+" "+time;
        return data;
    }
    return "";
}
void sendStoredData() {
    fstream file;
    file.open("data.txt",ios::in);
    if(file.is_open()) {
        string line;
        while(getline(file,line)) {
            PostJson(line);
        }
    } else {
        cout<<"File opening error"<<endl;
    }
    file.close();
    cout<<"Stored data sent"<<endl;
}
void connectionInit() {
    while(true) {
        connected = false;
        int connect = system("curl -I http://localhost:8080/SystemPerformance-Backend/storeStats");
        if(connect == 0)
            break;
        this_thread::sleep_for(5s);
    }
    sendStoredData();
    connected = true;
}
void deleteContentsOfFile() {
    fstream file;
    file.open("data.txt",ios::out | ios::trunc);
    file.close();
}
void curlInitialize() {
    
    if(curl_global_init(CURL_GLOBAL_ALL)) {
        fprintf(stderr, "Fatal: The initialization of libcurl has failed.\n");
        exit(0);
    }
    if(atexit(curl_global_cleanup)) {
        fprintf(stderr, "Fatal: atexit failed to register curl_global_cleanup.\n");
        curl_global_cleanup();
        exit(0);
    }
}
void storeAndSendData() {
    deleteContentsOfFile();
    fstream file;
    while(true) {
        string data = getStats();
        file.open("data.txt",ios::out | ios::in | ios::app);
        file << data;
        file << "\n";
        file.close();
        if(connected && data != "") {
            PostJson(data);
        }
        this_thread::sleep_for(300s);
    }
}
int main() 
{
    // cout<<"Enter System UUID number"<<endl;
    // cin>>uuid;
    // cout<<uuid<<endl;
    // cout<<"Length - "<<uuid.length()<<endl;
    curlInitialize();
    thread t1(storeAndSendData);
    connectionInit();
    while(true){}
}

