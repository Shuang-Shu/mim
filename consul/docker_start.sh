docker run -d \
  --name=consul \
  -p 8500:8500 \
  -p 8600:8600/udp \
  -v data:/consul/data \
  --restart=always \
  consul:1.15.4 agent -server -ui -client=0.0.0.0 -bootstrap-expect=1
