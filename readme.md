docker run -d --name ethereum -p 8545:8545 -p 30303:30303 ethereum/client-go  --rpc --rpcaddr "0.0.0.0" --rpcapi="db,eth,net,web3,personal" --rpccorsdomain "*" --testnet --fast
docker exec -it ethereum geth attach ipc:/root/.ethereum/testnet/geth.ipc

https://claudiodangelis.com/ethereum/2018/02/19/exploring-ethereum-platform-accounts.html