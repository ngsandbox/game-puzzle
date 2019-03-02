"use strict";
$(function () {
    $(".escape-area")
        .attr("title", "Run away from the arena")
        .click(onClickEscapeArea);
    $(".fight-area")
        .mouseenter(function () {
            $(this).removeClass("bg-light").addClass("bg-info")
        })
        .mouseleave(function () {
            $(this).addClass("bg-light").removeClass("bg-info")
        })
        .click(onClickFightArea);
});

function onClickFightArea() {
    let row = $(this).data("arena-row");
    let col = $(this).data("arena-col");
    $.ajax({
        contentType: 'application/json',
        data: JSON.stringify({
            "row": row,
            "col": col
        }),
        success: function (data) {
            console.log("New route received ", data);
        },
        error: renderError,
        processData: false,
        type: 'POST',
        url: '/api/v1/goto'
    });
}

function onClickEscapeArea() {

}

function onClickCreateSpecies() {
    if (!document.getElementById("createSpeciesForm").checkValidity()) {
        return;
    }

    $("#createSpeciesErrors").removeClass('d-block').addClass('d-none');
    let gender = $("input:radio[name='gender']:checked").val();

    $.ajax({
        contentType: 'application/json',
        data: JSON.stringify({
            "gender": gender,
            "strength": $("#strength").val(),
            "perception": $("#perception").val(),
            "endurance": $("#endurance").val(),
            "charisma": $("#charisma").val(),
            "intelligence": $("#intelligence").val(),
            "agility": $("#agility").val(),
            "luck": $("#luck").val()
        }),
        success: function (data) {
            console.log("species saved", data);
            disconnect();
            location.reload();
        },
        error: renderError,
        processData: false,
        type: 'POST',
        url: '/api/v1/goto'
    });
}

function renderError(e) {
    console.error("Error to call server ", e);
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