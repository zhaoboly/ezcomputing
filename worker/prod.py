#run command: python3 prod.py
import subprocess

import paramiko

def runCommand(command):
    print("start command: "+ command)
    p = subprocess.Popen(command, shell=True, stdout = subprocess.PIPE)
    stdout, stderr = p.communicate()
    #print (stdout.read())
    
    if p.returncode==0:
        print("succeed")
    else:
        print("failed: "+ command)
        quit()

print ("EZComputing Worker  -- starting deploying...")


runCommand("mvn package")
runCommand("docker build . -t 'zhaoboly/ezcomputing-worker'")
runCommand("docker login")
runCommand("docker push zhaoboly/ezcomputing-worker")

print("done.")
