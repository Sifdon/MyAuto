

const functions = require('firebase-functions');

const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.StadisticLog = functions.database.ref('/Proces').onWrite(event => {
    const snapshot = event.data;
    var tmp = snapshot.val();
    if(tmp == "Manteniment en proces"){
        var db = admin.database();
        var ref = db.ref("/Log");
        var dt = new Date();
        var month_inicial = dt.getMonth();
        var day_inicial = dt.getDate();
        var year_inicial = dt.getFullYear();
        ref.child("Data Inici").set({
            dia: day_inicial,
            mes: month_inicial,
            any: year_inicial
        });
    } else {
        var dt_final = new Date();
        var month_final = dt_final.getMonth();
        var day_final = dt_final.getDate();
        var year_final = dt_final.getFullYear();
        var string_dt_final = day_final + "-" + month_final + "-" + year_final;
        var month_inicial;
        var day_inicial;
        var year_inicial;
        var taller;
        var db = admin.database();
        var ref_dia_inicial = db.ref("/Log/Data Inici/dia");
        var ref_mes_inicial = db.ref("/Log/Data Inici/mes");
        var ref_any_inicial = db.ref("/Log/Data Inici/any");
        ref_dia_inicial.on("value", function(snapshot) {
                  console.log(snapshot.val());
                  day_inicial = snapshot.val();
                  console.log(day_inicial);
                  ref_mes_inicial.on("value", function(snapshot) {
                                            console.log(snapshot.val());
                                            month_inicial = snapshot.val();
                                            console.log(month_inicial);
                                            ref_any_inicial.on("value", function(snapshot) {
                                                                      console.log(snapshot.val());
                                                                      year_inicial = snapshot.val();
                                                                      console.log(year_inicial);
                                                                      var string_dt_inicial = day_inicial + "-" + month_inicial + "-" + year_inicial;
                                                                              var ref_taller = db.ref("/Taller");
                                                                              ref_taller.on("value", function(snapshot) {
                                                                                                        console.log(snapshot.val());
                                                                                                        taller = snapshot.val();
                                                                                                         var string_taller = "-Mantenimiento realizado en: " + taller + " desde: " + string_dt_inicial + " hasta: " + string_dt_final + ", Duración:";

                                                                                                                //Cálculo durada mantenimiento

                                                                                                                var dias = day_final - day_inicial;
                                                                                                                var meses = month_final - month_inicial;
                                                                                                                var horas;
                                                                                                                if(dias == 0 && meses == 0){
                                                                                                                    horas = 24;
                                                                                                                }else{
                                                                                                                    horas = (dias * 24) + (meses * 720);
                                                                                                                }

                                                                                                                string_taller = string_taller + " " + horas + " Horas.";

                                                                                                                var ref_history = db.ref("/Log/History/message");

                                                                                                                var msg;
                                                                                                                ref_history.on("value", function(snapshot) {
                                                                                                                                                  console.log(snapshot.val());
                                                                                                                                                  msg = snapshot.val();
                                                                                                                                                  var string_taller = "-Mantenimiento realizado en: " + taller + " desde: " + string_dt_inicial + " hasta: " + string_dt_final + ", Duración:";

                                                                                                                                                          //Cálculo durada mantenimiento

                                                                                                                                                          var dias = day_final - day_inicial;
                                                                                                                                                          var meses = month_final - month_inicial;
                                                                                                                                                          var horas;
                                                                                                                                                          if(dias == 0 && meses == 0){
                                                                                                                                                              horas = 24;
                                                                                                                                                          }else{
                                                                                                                                                              horas = (dias * 24) + (meses * 720);
                                                                                                                                                          }

                                                                                                                                                          string_taller = string_taller + " " + horas + " Horas.";

                                                                                                                                                          var ref_history = db.ref("/Log/History/message");

                                                                                                                                                          var msg;
                                                                                                                                                          ref_history.on("value", function(snapshot) {
                                                                                                                                                                                            console.log(snapshot.val());
                                                                                                                                                                                            msg = snapshot.val();
                                                                                                                                                                                            string_taller = msg + '\n' + string_taller;
                                                                                                                                                                                                    var ref = db.ref("/Log/History");
                                                                                                                                                                                                    ref.set({
                                                                                                                                                                                                                message: string_taller
                                                                                                                                                                                                    });
                                                                                                                                                                                          }, function (errorObject) {
                                                                                                                                                                                            console.log("The read failed: " + errorObject.code);
                                                                                                                                                                                          });
                                                                                                                                                }, function (errorObject) {
                                                                                                                                                  console.log("The read failed: " + errorObject.code);
                                                                                                                                                });
                                                                                                      }, function (errorObject) {
                                                                                                        console.log("The read failed: " + errorObject.code);
                                                                                                      });
                                                                    }, function (errorObject) {
                                                                      console.log("The read failed: " + errorObject.code);
                                                                    });
                                          }, function (errorObject) {
                                            console.log("The read failed: " + errorObject.code);
                                          });
                }, function (errorObject) {
                  console.log("The read failed: " + errorObject.code);
        });
    }
});

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
                 body: "Don't forget !!"
               }
   };
   const DeviceTokensPromise = admin.database().ref('tokendevices').once('value');
   return Promise.all([DeviceTokensPromise]).then(results =>{
        const tokenSnapshot = results[0];
        if(!tokenSnapshot.hasChildren()){
            return console.log('There are no notification tokens to send to.');
        }
        const tokens = Object.keys(tokenSnapshot.val());
        const t = tokenSnapshot.ref.child(tokens[0]).once('value');
        return Promise.all([t]).then(res => {
            const to = res[0];
            const tok = to.val();
            console.log(tok);
            return admin.messaging().sendToDevice(tok, payload).then(response => {
               const tokensToRemove = [];
               response.results.forEach((result,index) => {
                    const error = result.error;
                    if(error){
                       console.error('Failure sending notification to', tokens[index], error);
                       if(error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered'){
                          tokensToRemove.push(tokenSnapshot.ref.child(tokens[index]).remove());
                       }
                    }
               });
               Promise.all(tokensToRemove);
            });

        });
   });

 }
});
