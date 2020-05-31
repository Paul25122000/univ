const LogFolder = './Logs/',
  fs = require('fs'),
  readline = require('readline'),
  fetch = require("node-fetch"),
  API = "http://192.168.1.100:3000";

var sendLog = (msg) => {
  msg = msg.split('-');
  let data_light = {
    "time": msg[0],
    "value": msg[1],
    "set": msg[2]
  },
  data_temperature = {
    "time": msg[0],
    "value": msg[3],
    "set": msg[4]
  },
  data_water = {
    "time": msg[0],
    "value": msg[5],
    "set": msg[6]
  }
  fetch(API+'/light', {
    headers: { 'Content-Type': 'application/json' },
    method: "POST",
    body: JSON.stringify(data_light)
  })
  fetch(API+'/temperature', {
    headers: { 'Content-Type': 'application/json' },
    method: "POST",
    body: JSON.stringify(data_temperature)
  })
  fetch(API+'/water', {
    headers: { 'Content-Type': 'application/json' },
    method: "POST",
    body: JSON.stringify(data_water)
  })
},
  rd;

fs.readdir(LogFolder, (dirReadErr, files) => {
  if (dirReadErr) throw dirReadErr;
  files.forEach(file => {
    if (file.includes("log")) {
      rd = readline.createInterface({
        input: fs.createReadStream(LogFolder + file)
      });
      rd.on('line', function (line) {
        console.log(line)
        sendLog(line.toString())
      });
    }
  });
});