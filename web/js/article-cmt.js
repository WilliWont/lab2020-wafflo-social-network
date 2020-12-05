$(document).on("submit", "#commentForm", function(event) {
    var $form = $(this);
    $.post("DispatchServlet?btnAction=Post Comment", $form.serialize(), function(response) {
        if(response.error){
            alert("error: "+ response.error);
        } else {
            createComment(response);
        }
    });

    event.preventDefault(); // Important! Prevents submitting the form.
});

function createComment(response){
    $("#commentTextArea").val('');
    $("#cmt-empty").text('');

    let $cmt = $("#cmt-template").clone().removeAttr('id');
    
    $cmt.children(".div-cmt-head").children('.cmt-owner').text(response.name+':');
    
    let a = $cmt.children(".div-cmt-head")
    .children('a').attr('href')+'&txtCmtID='+response.id;

    $cmt.children(".div-cmt-head").children('a').attr('href',a);

    $cmt.children(".div-cmt-body").text(response.description);

    $("#div-comment").prepend($cmt);
}