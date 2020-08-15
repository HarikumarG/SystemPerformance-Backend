#include<bits/stdc++.h>
#include<sys/sysinfo.h>
#include<thread>
#include<sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <sys/time.h>
#include <arpa/inet.h>
using namespace std;

#define PORT 8081
#define max_clients 30

int client_sockets[max_clients];

void sendDatatoAllclients() {
	struct sysinfo si;
	while(true) {
		if(sysinfo(&si) != -1) {
			string totalram = to_string(si.totalram);
			string freeram = to_string(si.freeram);
			string usedram = to_string(si.totalram-si.freeram);
			string totalswap = to_string(si.totalswap);
			string freeswap = to_string(si.freeswap);
			string usedswap = to_string(si.totalswap-si.freeswap);
			string loadavgpast1 = to_string(si.loads[0]/65536.0);
			string loadavgpast5 = to_string(si.loads[1]/65536.0);
			string loadavgpast15 = to_string(si.loads[2]/65536.0);
			string data = totalram+"/"+freeram+"/"+usedram+"/"+totalswap+"/"+freeswap+"/"+usedswap+"/"+loadavgpast1+"/"+loadavgpast5+"/"+loadavgpast15+"\n";
			const char *buffer = data.c_str();

			for(int i = 0; i < max_clients; i++) {
				if(client_sockets[i] != 0) {
					send(client_sockets[i], buffer, strlen(buffer), 0);
					cout<<"Data sent to "<<client_sockets[i]<<endl;
				}
			}
		}
		this_thread::sleep_for(10s);
	}
}

int main() 
{
	for(int i = 0; i < max_clients; i++)
		client_sockets[i] = 0;

	thread t1(sendDatatoAllclients);

	int master_socket;
	if( (master_socket = socket(AF_INET , SOCK_STREAM , 0)) == 0)
    {
        cout<<"Socket failed"<<endl;
        return -1;
    }

    int opt = 1;
    if( setsockopt(master_socket, SOL_SOCKET, SO_REUSEADDR, (char *)&opt, sizeof(opt)) < 0 )
    {
     	cout<<"Set socket opt failed"<<endl;
     	return -1;
    }

    sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons( PORT );

    if (bind(master_socket, (struct sockaddr *)&address, sizeof(address))<0)
    {
    	cout<<"Bind failed"<<endl;
    	return -1;
    }   
    cout<<"Listener on port "<<PORT<<endl;

    if( listen(master_socket, 3) < 0)
    {
    	cout<<"Listen error"<<endl;
    	return -1;
    }

    int addrlen = sizeof(address);
    cout<<"Waiting for Connections ...."<<endl;

    fd_set readfds;
    int max_sd;
    while(true) {

    	FD_ZERO(&readfds);
    	FD_SET(master_socket, &readfds);
    	max_sd = master_socket;

    	for(int i = 0; i < max_clients; i++) {
    		if(client_sockets[i] > 0) 
    			FD_SET(client_sockets[i], &readfds);
    		if(client_sockets[i] > max_sd)
    			max_sd = client_sockets[i];
    	}
        int activity = select( max_sd + 1 , &readfds , NULL , NULL , NULL);

        if((activity < 0) && (errno != EINTR))
        	cout<<"Select error"<<endl;

        if( FD_ISSET(master_socket, &readfds))
        {
        	int new_socket = accept(master_socket,(struct sockaddr *)&address, (socklen_t*)&addrlen);
        	if( new_socket < 0) {   
                cout<<"Accept error"<<endl;   
                return -1;  
            }   
            cout<<"New Connection socket_fd is "<<new_socket<<" ip is : "<<inet_ntoa(address.sin_addr)<<" port is "<<ntohs(address.sin_port)<<endl;

            for (int i = 0; i < max_clients; i++)
            {
                if(client_sockets[i] == 0)
                {   
                    client_sockets[i] = new_socket;   
                    cout<<"Adding to list of sockets as "<<client_sockets[i]<<endl;                         
                    break;
                }
            }
        }
        for(int i = 0; i < max_clients; i++) {
        	
        	int sd = client_sockets[i];

        	if(FD_ISSET(sd, &readfds))
        	{
        		int valread;
        		char buffer[1024];
        		if ((valread = read( sd , buffer, 1024)) == 0)
            	{   
                	getpeername(sd, (struct sockaddr*)&address, (socklen_t*)&addrlen);
                	cout<<"Disconnected "<<sd<<" ip is : "<<inet_ntoa(address.sin_addr)<<" port is "<<ntohs(address.sin_port)<<endl;
                	close(sd);
                	client_sockets[i] = 0;
            	}
            	else {
             		cout<<buffer;
            		char message[] = "Hello from the server!";
					if(send(sd, message, strlen(message), 0) != strlen(message))
            		{   
                		cout<<"Send error"<<endl;
            		}   
            		cout<<"Welcome message sent successfully"<<endl;
            	}
        	}
        }
    }
}

