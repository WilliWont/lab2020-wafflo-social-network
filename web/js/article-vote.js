function updateVoteDiv(voteValue){
    if(voteValue === 1){
        $("#upVoteValue").text(baseUpVote + 1);
        $("#downVoteValue").text(baseDownVote);
        $("#upVoteValue").addClass("upvote-clicked");
        $("#downVoteValue").removeClass("downvote-clicked");
        $("#btn-Downvote").removeClass("btn-downvote-clicked");
        $("#btn-Upvote").addClass("btn-upvote-clicked");
    }
    else if(voteValue === -1){
        $("#downVoteValue").text(baseDownVote-1);
        $("#upVoteValue").text(baseUpVote);
        $("#upVoteValue").removeClass("upvote-clicked");
        $("#downVoteValue").addClass("downvote-clicked");
        $("#btn-Downvote").addClass("btn-downvote-clicked");
        $("#btn-Upvote").removeClass("btn-upvote-clicked");
    }
    else
    {
        $("#upVoteValue").text(baseUpVote);
        $("#downVoteValue").text(baseDownVote);
        $("#upVoteValue").removeClass("upvote-clicked");
        $("#downVoteValue").removeClass("downvote-clicked");
        $("#btn-Downvote").removeClass("btn-downvote-clicked");
        $("#btn-Upvote").removeClass("btn-upvote-clicked");
    }
}

function sendRequest(txtVote, myVote){
    $.get("DispatchServlet?btnAction=Vote&txtVote="+txtVote+
            "&txtArticleID="+articleID, function(responseValue) {   
        if(responseValue.includes("unavailable")){
            $("#msg-error").text(responseValue);
        } 
        else 
        {
            updateVoteDiv(myVote);
            pageVote = myVote;
        }
    });
}

$(document).on("click", "#btn-Upvote", function() { 
    console.log("upvote-clicked");
    let myVote = 1;
    let txtVote = "up";
    if(pageVote === 1){
        txtVote = "unvote";
        myVote = 0;
    }
    
    sendRequest(txtVote, myVote);
});

$(document).on("click", "#btn-Downvote", function() {
    console.log("downvote-clicked");
    let myVote = -1;
    let txtVote = "down";
    if(pageVote === -1){
        txtVote = "unvote";
        myVote = 0;
    }
    
    sendRequest(txtVote, myVote);
});
