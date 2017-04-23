const functions = require('firebase-functions');

const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

// Sends a notifications to all users when a new message is posted.

// Sends a notifications to all users when a new message is posted.
exports.sendNotifications = functions.database.ref('/Oli Contador').onWrite(event => {
  const snapshot = event.data;

  // Notification details.
  var tmp = snapshot.val();
  //var km = parseInt(tmp) + "<br>";
  var change = tmp / 1000;


 if( 14500 < change ){
   console.log('Change the Oil');

   const payload = {
               notification: {
                 title: 'You have to change the Oil' ,
                 body: 'No te olvides !!'
               }
             };


                     // Send notifications to all tokens.
              return admin.messaging().sendToDevice("dBvAJlmvA3I:APA91bG2fT8jU2kyfzuREL1A3xg0_dTPmOEX_r95l_7hmv23PCYDAh6YXdDMQGyfUFyIQYvJIntSJedLMFICjCOyAF_aL0vi3zLA6t9lHMz81RXQE7ePj0VK3iVqYMSC86FJqZwm5mKE", payload).then(response => {
                   });
 }else{
   return;
 }





});

// // Start writing Firebase Functions
// // https://firebase.google.com/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
//



