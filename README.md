# we-scripts

Various scripts for working with Waves Enterprise

## Run

### Getting information about current height on Mainnet nodes (Fork check)

```sh
bb fork-check-mainnet.clj
```

### RIDE Compiler on node

```sh
bb ride-compiler.clj -u https://voting-node-0.we.vote -p simple.ride
```

### Getting approximate range of blocks by timestamp
```sh
bb get-block-by-time.clj -u https://voting-node-0.we.vote -t 1719532885100
```

### Getting information about block range
```sh
bb get-blocks-headers-seq.clj -u https://voting-node-0.we.vote -f 1 -t 1000
```
