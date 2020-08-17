#include<bits/stdc++.h>
#include<sys/sysinfo.h>
#include<thread>
#include<sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
// #include <sys/time.h>
#include <arpa/inet.h>
using namespace std;

#define PORT 8081
int client_socket = 0;
bool connected = false;
string getStats(string type) {
    struct sysinfo si;
    if(sysinfo(&si) != -1) {
        string uptime = to_string(si.uptime);
        string totalram = to_string(si.totalram);
        string freeram = to_string(si.freeram);
        string usedram = to_string(si.totalram-si.freeram);
        string totalswap = to_string(si.totalswap);
        string freeswap = to_string(si.freeswap);
        string usedswap = to_string(si.totalswap-si.freeswap);
        string loadavgpast1 = to_string(si.loads[0]/65536.0);
        string loadavgpast5 = to_string(si.loads[1]/65536.0);
        string loadavgpast15 = to_string(si.loads[2]/65536.0);
        //if(type == "socket") {
            string data = uptime+"/"+totalram+"/"+freeram+"/"+usedram+"/"+totalswap+"/"+freeswap+"/"+usedswap+"/"+loadavgpast1+"/"+loadavgpast5+"/"+loadavgpast15+"/"+type+"\n";
            return data;            
        // }
        // else {
        //     string data = uptime+"/"+totalram+"/"+freeram+"/"+usedram+"/"+totalswap+"/"+freeswap+"/"+usedswap+"/"+loadavgpast1+"/"+loadavgpast5+"/"+loadavgpast15+"/"+"hari\n";
        //     return data;
        // }
    }
    return "";
}

void storeAndSendData() {
    fstream file;
	while(true) {
        string data = getStats("socket");
	    file.open("data.txt",ios::out | ios::in | ios::app);
        file << data;
        file.close();
		const char *buffer = data.c_str();
		if(connected) {
			send(client_socket, buffer, strlen(buffer), 0);
			cout<<"Data sent to "<<client_socket<<endl;
		} else {
            cout<<"Socket is not connected"<<endl;
        }
        this_thread::sleep_for(20s);
	}
}
void sendStoredData() {
    this_thread::sleep_for(1s);
    fstream file;
    file.open("data.txt",ios::in);
    if(!connected) {
        cout<<"Socket is not connected"<<endl;
    } else {
        if(file.is_open()) {
            string line;
            const char *buffer;
            while(getline(file,line)) {
                line = line + "\n";
                buffer = line.c_str();
                send(client_socket,buffer,strlen(buffer),0);
            }
        }
        file.close();
        cout<<"Stored data sent"<<endl;
    }
}
bool checkChar(char s) {
    if((s >= 'a' && s <='z') || (s >= 'A' && s <= 'Z'))
        return true;
    return false;
}
int main() 
{

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
    connected = true;
    sendStoredData();

    char buffer[1024];
    int readval;

    while((readval = read(client_socket, buffer, 1024)) != 0) {
        if(strlen(buffer) > 0) {
            string s;
            for(int i = 0; i < strlen(buffer); i++)
                if(checkChar(buffer[i]))
                    s = s + buffer[i];
            cout<<s<<endl;
            string data = getStats(s);
            const char* sendBuffer = data.c_str();
            send(client_socket, sendBuffer, strlen(sendBuffer), 0);
        }
    }
}

