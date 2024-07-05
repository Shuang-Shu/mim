idl: # 基于idl生成代码
	- mkdir temp
	mv src/main/java/com/mdc/mim/thrift temp 
	- rm -rf src/main/java/com/mdc/mim
	- mkdir -p src/main/java/com/mdc/mim
	mv temp/thrift src/main/java/com/mdc/mim
	rm -rf temp
	thrift -out src/main/java -r --gen java src/main/resources/idl/rpc.thrift
build:
	make idl

.PHONY: idl build