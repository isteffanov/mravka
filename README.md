# in memory cache store

## TODO:
    Transducer Strategies:
    * Array State map with swapping
    * BTree Map for cache hits
    * Unrolled Linked List for linearity but with cache hits
    * command-line arguments when compiling for those
---
    Transducer Testing
    * Additional tests for every code path in the code
    * Speed testing
    * Benchmarking engine

--- 
    Strategies as new BackedStores  
    * BTreeBackedStore

---
    Interfacing the cache
    * API endpoints to use the cache on the web
    * another cli project to connect to the backend here and do operations
        - parsing <<3
    * TTL in values

--- 
    Math-proofing hard
    * allow for different key and value types with monoids
    * formal verification for the algorithms

--- 
    Lifecycle and CI/CD
    * checkstyle
    * running tests on merge/push
    * running verification on merge/push
