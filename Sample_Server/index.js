var express = require('express');
var bodyParser = require('body-parser')
var awesome = require('./modules/awesome-api.js');
const formidable = require('formidable');
const fs = require('fs');
var app = express();

function parseForm(req, res, next) {
    var form = new formidable.IncomingForm();
    var requestForGithub = {
        files: []
    }
    form.parse(req);
    form.on('field', function(name, value) {
        console.log(name, value);
        requestForGithub[name] = value;
    });
    form.on('fileBegin', function (name, file){
        file.path = __dirname + '/uploads/' + file.name;
    });
    form.on('file', function(name, file) {
        requestForGithub.files.push(file);
    });
    form.on('end', function() {
        // TODO: weiterleiten an github
        console.log(requestForGithub);
        // Files werden aus temporärem upload gelöscht (keine unnötigen Daten liegen herum, Doppelbenennungen werden vermieden)
        requestForGithub.files.forEach(file => {
            fs.unlinkSync(file.path);
        })
        req.body = requestForGithub;
        next();
    });
}

// das ist eine Middlewarefunktion
function checkMessageEmpty(req, res, next) {
    var body = req.body;
    console.log(body);
    if(!body.nachricht) {
        res.status(400).send("Bad Request!");
    } else {
        next();
    }
}

app.get('/user/:userid/stuff', function (req, res) {
  var userid = req.params.userid;
  console.log(userid);
  res.send('Hello World!');
});

app.post('/user/:userid/stuff',
    parseForm,
    checkMessageEmpty,
    function(req, res) {
  var userid = req.params.userid;
  var body = req.body;
  console.log(body.nachricht);
  res.send('Alles gut');
})

app.get('/user/:userid/stuff/awesome',
    awesome.makeStuffA,
    awesome.makeStuffB
)

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});