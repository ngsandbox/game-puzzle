"use strict";
(function () {
    'use strict';
    window.addEventListener('load', function () {
        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        const forms = document.getElementsByClassName('needs-validation');
        // Loop over them and prevent submission
        Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#createSpeciesBtn").click(onClickCreateSpecies);
});

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
            location.reload();
        },
        error: function (e) {
            console.error("Error to save species ", e);
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

            $("#createSpeciesErrors")
                .html(errorMessage)
                .removeClass('d-none')
                .addClass('d-block');
        },
        processData: false,
        type: 'POST',
        url: '/api/v1/species'
    });

}