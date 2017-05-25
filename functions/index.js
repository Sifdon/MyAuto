const functions = require('firebase-functions');

const admin = require('firebase-admin');

/*var Sequelize = require('sequelize');

var sequelize = new Sequelize(undefined, undefined, undefined, {
    'dialect': 'sqlite',
    'storage': __dirname + '/myauto.sqlite'
});

var Data = sequelize.define('data', {
    m: {
        type: Sequelize.INTEGER
    },
    d: {
        type: Sequelize.INTEGER
    },
    y: {
        type: Sequelize.INTEGER
    }
})

var Taller = sequelize.define('taller', {
    t: {
        type: Sequelize.STRING
    }
})

*/


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
/*
        var tmp = db.ref("/Log/data_inici/valors/any");
        tmp.on("value", function(snapshot) {
          console.log(snapshot.val());
          var taller = snapshot.val();
        }, function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });

        var dt = new Date();
        var month = dt.getMonth();
        var day = dt.getDate();
        var year = dt.getFullYear();

        sequelize.sync({force: true}).then(function() {
            console.log('Everything is synced');

            Data.create({
                m: month,
                d: day,
                y: year
            }).then(function (data){
                console.log('Finished!');
                console.log(data);
            });

            Taller.create({
                t: taller
            }).then(function (taller){
                console.log('Finished!');
                console.log(taller);
            });
        });*/

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





        /*Data.findAll().then(function (data) {

            data.forEach(function(elemento) {

                var month_inicial = elemento.dataValues.m;
                var day_inicial = elemento.dataValues.d;
                var year_inicial = elemento.dataValues.y;

            });
            var string_dt_inicial = day_inicial + "-" + month_inicial + "-" + year_inicial;

        });



        Data.destroy({
            where: {}
        }).then(function(){
            console.log('destroy all data');
        })*/



    }


});

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



