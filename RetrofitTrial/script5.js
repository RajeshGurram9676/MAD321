var express = require('express');
const app =express();

var bodyparser =require("body-parser");
var mysql = require('mysql');
const db = mysql.createConnection({
    host:"localhost",
    user:"root",
    password:"",
    database:'inventory'
});
db.connect((err)=>{
    if(err){
        throw err;
    }
    console.log("connected!!");
});
app.use(bodyparser.json());
app.use(bodyparser.urlencoded({extended:true}));

app.post('/register/',(req,res,next)=>{

    var data=req.body;
    var username= data.username;
    var phone= data.phone;
    var email=data.email;
    var password= data.password;

    db.query("SELECT * FROM registration WHERE email= ?",[email],function(err,result,fields){

        db.on('error',(err)=>{
            console.log("[mysql error]",err);
        });

        if(result && result.length){
            res.json("User exists");
        }
        else{
            var insert_cmd ="INSERT INTO registration (username,phone,email,password) values (?,?,?,?)";
            var values=[username,phone,email,password];
            console.log(result);
            console.log("executing:" + insert_cmd + "" + values);
    
            db.query(insert_cmd,values,(err,results,fields)=>{
                db.on("err",(err)=>{
                    console.log("[mysql error]",err);
                });
                res.json("registered !");
                console.log("successful.");
            });
        }


    });

});

app.post('/login/',(req,res,next)=>{

    var data=req.body;
    var name= data.name;
    var email=data.email;
    var password= data.password; 

    db.query("SELECT * FROM registration WHERE email= ?",[email],function(err,result,fields){

        db.on('error',(err)=>{
            console.log("[mysql error]",err);
        });

        if(result && result.length){
            console.log(result);
     
            if(password==result[0].password){
             res.json("user logged in");
             res.end;
     
            }
            else{
                res.json("wrong password");
                res.end;
            }
        }
         else{
             res.json("User not found");
             res.end;
        }


    });

});
app.get("/getdata",(req,res,next)=>{
    db.query("SELECT * FROM registration",function(err,result,fields){
        db.on('error',function(err){
            console.log("[MySQL ERROR]",err);
        });

        if(result&&result.length)
        {
            res.json(result);
            res.end;
        }
        else
        {
            res.json("User not Found");
            res.end;
        }
    });
});
app.post('/product/',(req,res,next)=>{

    var data=req.body;
    var pname= data.pname;
    var descr=data.descr;
    var pid= data.pid;

    db.query("SELECT * FROM productss WHERE pname=?",[pname],function(err,result,fields){

        db.on('error',(err)=>{
            console.log("[mysql error]",err);
        });

        if(result && result.length){
            res.json("Product exist");
        }
        else{
            var insert_pro ="INSERT INTO productss (pid,pname,descr) ssvalues (?,?,?)";
            var values=[pid,pname,descr];
            console.log(result);
            console.log("executing:" + insert_pro + "" + values);
    
            db.query(insert_pro,values,(err,results,fields)=>{
                db.on("err",(err)=>{
                    console.log("[mysql error]",err);
                });
                res.json("product added !");
                console.log("successful.");
            });
        }


    });

});

var server= app.listen(3008,function(){
    var port=server.address().port;
    console.log("server is running at http://localhost:%s",port);
});