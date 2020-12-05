const NOTIFICATION_INTERVAL = .5;

// Interval in Minutes
let interval = 1000 * 60 * NOTIFICATION_INTERVAL;

let notif_function = function() {
    $.get("DispatchServlet?btnAction=Check Notification", function(responseValue) {
            if(responseValue > 0){
                $("#notif-amount").addClass('notif-full');
                $("#notif-amount").removeClass('notif-empty');
            }
            else {
                $("#notif-amount").addClass('notif-empty');
                $("#notif-amount").removeClass('notif-full');
            }
            
            $("#notif-amount").text(responseValue);
    });
};

notif_function();
setInterval(notif_function, interval);