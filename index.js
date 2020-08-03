//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


//Notification for new task:
exports.newTask = functions.database.ref('/Airports/Task/{task_id}').onCreate((snapshot) => {
  var data = snapshot.val();
  console.log('Data : ', data);
  var task_id = snapshot.child('membersId').val();
  console.log('Id is : ', task_id)

  var all_tokens = new Array();

  for(id in task_id){
    var ref = admin.database().ref('/Airports/Users').child(task_id[id]).child('device_token');

    ref.on("value", function(snapshot) {
       all_tokens.push(snapshot.val());
    }, function (error) {
       console.log("Error: " + error.code);
    });
    console.log(all_tokens[0]);
    console.log(all_tokens[1]);
    
  }

  const payload = {
    notification: {
        title: data.title,
        body: 'You have a new task.',
        sound: "default"
    }
};

const options = {
  priority: "high",
  timeToLive: 60 * 60 * 24
};

return admin.messaging().sendToDevice(all_tokens, payload, options);
  
});

//Notification for new comment:
exports.newComment = functions.database.ref('/Airports/Task/{taskId}/Comments/{comment_id}').onCreate((snapshot, context) => {
  var data = snapshot.val();
  console.log('Data : ', data)
  const taskId = context.params.taskId;
  console.log('taskId : ', taskId);

  var t = new Array();

  var refDB = admin.database().ref('/Airports/Task').child(taskId).child('membersId');

  var user_id;
  refDB.on("value", function(snapshot) {
    user_id = snapshot.val();

    for(id in user_id){
      var ref = admin.database().ref('/Airports/Users').child(user_id[id]).child('device_token');
   
      ref.on("value", function(snapshot) {
         t.push(snapshot.val());
      });
      //console.log(tk[0]);
      //console.log(tk[1]);
      
    }
    console.log('user_id : ', snapshot.val());
 });

  //console.log('tk_user: ', tk);

if(data.type === "image"){
  const payload = {
    notification: {
        title: 'New Message',
        body: 'image',
        sound: "default"
    }
};

} else {
  const payload = {
  notification: {
      title: 'New Message',
      body: data.comment,
      sound: "default"
  }
};

}

const options = {
  priority: "high",
  timeToLive: 60 * 60 * 24
};

console.log('Completed');
return admin.messaging().sendToDevice(t, payload);

});

  //Notification for dwell time triggers
  exports.newTrigger = functions.database.ref('/Airports/Dwell_Time_Triggers/{trigger_id}').onCreate((snapshot, context) => {
    var data = snapshot.val();
    console.log('Data : ', data)
    const trigger_id = context.params.trigger_id;
    console.log('triggerId : ', trigger_id);
    console.log('trigger level : ', data.trigger_level);
  
    var refDB = admin.database().ref('/Airports/EscalationMatrix').child(data.trigger_level).child('user_id');
  
    var user_id;
    var token_id;
    refDB.on("value", function(snapshot) {
      user_id = snapshot.val();
      console.log('USER ID : ', user_id);
  
      var ref = admin.database().ref('/Airports/Users').child(user_id).child('device_token');
      
      ref.on("value", function(snapshot) {
           token_id = snapshot.val();
      });
      console.log('user_id : ', snapshot.val());
   });
  
    //console.log('tk_user: ', tk);
  
    const payload = {
      notification: {
          title: 'Dwell Time Exceeded',
          body: 'There is an overload at ' + data.touchpoint,
          sound: "default"
      }
  
  }
  
  const options = {
    priority: "high",
    timeToLive: 60 * 60 * 24
  };
  
  console.log('Completed');
  return admin.messaging().sendToDevice(token_id, payload);
  
  });