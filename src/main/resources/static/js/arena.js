"use strict";
$(function () {
    $(".fight-area")
        .mouseenter(function () {
            $(this).removeClass("bg-light").addClass("bg-info")
        })
        .mouseleave(function () {
            $(this).addClass("bg-light").removeClass("bg-info")
        })
        .click(onClickFightArea);
});

let atacker = null;

function onClickFightArea() {
    if (atacker) {
        console.log("The move does not complete yet");
        return;
    }

    let row = $(this).data("arena-row");
    let col = $(this).data("arena-col");
    atacker = {
        "row": row,
        "col": col
    };
    $.ajax({
        contentType: 'application/json',
        data: JSON.stringify({
            "row": row,
            "col": col
        }),
        success: function (data) {
            console.log("New route received ", data);
            atacker = null;
            changePosition(data);
        },
        error: renderError,
        dataType: "json",
        processData: false,
        type: 'POST',
        url: '/api/v1/goto'
    });
}

function changePosition(data) {
    if (data) {
        if (data.next && data.route) {
            let img = $((!!data.userHit) ? ".fight-user" : ".fight-enemy").parent();
            let life = $((!!data.userHit) ? ".enemy-life" : ".user-life");
            let timeout = 200;
            data.route.forEach(function (r) {
                setTimeout(function () {
                    $("td[data-arena-row='" + r.row + "'][data-arena-col='" + r.col + "']").append(img)
                }, timeout);
                timeout += 200;
            });

            if (data.damage) {
                let damage = parseInt(data.damage);
                let maxLife = parseInt(life.attr("aria-valuemax"));
                let minLife = parseInt(life.attr("aria-valuemin"));
                let prevLife = parseInt(life.attr("aria-valuenow"));
                let pct = ((maxLife - minLife) / 100);
                let newLife = prevLife - damage;
                life.attr("aria-valuenow", newLife);
                let nowPct = parseInt(newLife / pct);
                life.css({"width": nowPct + "%"});
            }

            if(data.userHit){
                $(".user").removeClass("text-primary");
                $(".enemy").addClass("text-primary");
            } else{
                $(".enemy").removeClass("text-primary");
                $(".user").addClass("text-primary");
            }

            if(data.finish){
                window.location.href = '/game';
            }
        }
    }
}

function renderError(e) {
    console.error("Error to call server ", e);
    atacker = null;
    let errorMessage = e.responseText;
    if (errorMessage) {
        try {
            let errorDetails = JSON.parse(errorMessage);
            if (errorDetails && errorDetails.length) {
                errorMessage = "";
                errorDetails.forEach(function (detail) {
                    errorMessage += detail.details + "<br />";
                });
            }
        } catch (ex) {
            console.warn("Could not parse json from error details", ex);
            errorMessage = e.responseText;
        }
    } else {
        errorMessage = e.statusText;
    }

    $("#fightErrors")
        .html(errorMessage)
        .removeClass('d-none')
        .addClass('d-block');

}