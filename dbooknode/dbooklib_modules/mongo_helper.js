const csv = require('csv-parser');
const fs = require('fs');
const assert = require('assert')
const MongoClient = require('mongodb').MongoClient
const MONGO_CONN_STR = 'mongodb://127.0.0.1:27017'
const DB_NM = 'librarydb'



const loadbookshelf = () => {
    
    const results = []

    // var toInsert = {category: "文学", name: "海伯利安 3",year: "2019",source: "图书馆",user: "Dylan",read: true}


    fs.createReadStream('./data/bookshelfdata.csv').pipe(csv()).on('data', (row) =>{
        // console.log(row)
        results.push(row)
    }).on('end', ()=>{
        const client = new MongoClient(MONGO_CONN_STR, { useUnifiedTopology: true })
        client.connect(function(err){
            assert.equal(null, err);
            console.log('Connected successfully to server: '+ MONGO_CONN_STR)
        })

        client.db(DB_NM).collection('user_read_info').insertMany(results, function(err, docs){
            assert.equal(null, err)
            console.log(docs.insertedIds)
            client.close()
        })
        console.log('\ndbooklib processing end.')
    })   
}

const loadreadinginfo2db = () => {
    const client = new MongoClient(MONGO_CONN_STR, { useUnifiedTopology: true })
    var collection_read_info = null;
    // console.log(readingInfo);
    client.connect(function(err){
        assert.equal(null, err);
        console.log('Connected successfully to server: '+ MONGO_CONN_STR)
        collection_read_info = client.db(DB_NM).collection('user_read_info')
        

        // collection_book.find({}).toArray(function(err, docs){
        //     assert.equal(null, err)
        //     console.log(docs)
        // })
        
    })

}

const test_get_book = () => { 
    console.log(MONGO_CONN_STR)
    const client = new MongoClient(MONGO_CONN_STR, { useUnifiedTopology: true })
    client.connect(function(err){
        assert.equal(null, err);
        console.log('Connected successfully to server: '+ MONGO_CONN_STR)
        const collection_book = client.db(DB_NM).collection('book')

        collection_book.find({}).toArray(function(err, docs){
            assert.equal(null, err)
            console.log(docs)
        })
        client.close()
    })
    
}



module.exports = {
    loadbookshelf: loadbookshelf,
    test_get_book: test_get_book,
    loadreadinginfo2db: loadreadinginfo2db
}