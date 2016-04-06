wget http://mirror.sdunix.com/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
tar xvzf apache-maven-3.3.9-bin.tar.gz
mv apache-maven-3.3.9 /usr/local
cd /usr/local
ln -s apache-maven-3.0.5 maven
sudo cat maven.sh >> /etc/profile.d/maven.sh
