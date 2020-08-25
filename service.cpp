#include <bits/stdc++.h>
#include <sys/sysinfo.h>
#include <thread>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <time.h>
using namespace std;

#define PORT 8081
int client_socket = 0;
bool connected = false;
time_t rawtime;
char dateChar[11];
char timeChar[9];
struct tm *timeinfo;

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
        string data = date+" "+time+"/"+totalram+"/"+usedram+"/"+cpuusage+"\n";
        return data;
    }
    return "";
}

void storeAndSendData() {
    fstream file;
	while(true) {
        string data = getStats();
	    file.open("data.txt",ios::out | ios::in | ios::app);
        file << data;
        file.close();
		const char *buffer = data.c_str();
		if(connected && data != "") {
			send(client_socket, buffer, strlen(buffer), 0);
			cout<<"Data sent to "<<client_socket<<endl;
		} else {
            cout<<"Socket is not connected so Store it"<<endl;
        }
        this_thread::sleep_for(10s);
	}
}
void sendStoredData() {
    this_thread::sleep_for(1s);
    fstream file;
    file.open("data.txt",ios::in);
    if(file.is_open()) {
        string line;
        const char *buffer;
        while(getline(file,line)) {
            line = line + "\n";
            buffer = line.c_str();
            send(client_socket,buffer,strlen(buffer),0);
        }
    } else {
        cout<<"File opening error"<<endl;
    }
    file.close();
    cout<<"Stored data sent"<<endl;
}
void deleteContentsOfFile() {
    fstream file;
    file.open("data.txt",ios::out | ios::trunc);
    file.close();
}
int main() 
{
    deleteContentsOfFile();
    thread t1(storeAndSendData);
    sockaddr_in address;
    if((client_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        cout<<"Socket creation error"<<endl;
        return -1;
    }

    address.sin_family = AF_INET;
    address.sin_port = htons(PORT);

    if(inet_pton(AF_INET, "127.0.0.1", &address.sin_addr)<=0)
    { 
        cout<<"Invalid address (ie) Address not supported"<<endl; 
        return -1; 
    }

    while(true) {
        int val = connect(client_socket, (struct sockaddr *)&address, sizeof(address));
        if(val == 0)
            break;
        cout<<"Connection failed"<<endl;
        this_thread::sleep_for(2s);
    }
    cout<<"Connected"<<endl;
    sendStoredData();
    connected = true;
    while(true){}
    // char buffer[1024];
    // int readval;

    // while((readval = read(client_socket, buffer, 1024)) != 0) {
    //     if(strlen(buffer) > 0) {
    //         string s;
    //         for(int i = 0; i < strlen(buffer); i++)
    //             if(checkChar(buffer[i]))
    //                 s = s + buffer[i];
    //         cout<<s<<endl;
    //         string data = getStats(s);
    //         const char* sendBuffer = data.c_str();
    //         send(client_socket, sendBuffer, strlen(sendBuffer), 0);
    //     }
    // }
}

