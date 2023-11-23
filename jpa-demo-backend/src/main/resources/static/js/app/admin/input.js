function init() {
    initValidator();
}

function bind() {

    $("#btnReset").on("click", function (e) {
        $("form").trigger("reset");
        clearOldValidationErrorMessages();
    });

    $("#btnCancel").on("click", function (e) {
        goToHomePage();
    });
}

function initValidator() {
    $("#frmAdministrator").validate({
        rules: {
            "name": {
                required: true,
                maxlength: 50
            },
            "loginId": {
                required: true,
                maxlength: 50
            },
            "password": {
                required: true,
                minlength: 8
            },
            "confirmPassword": {
                required: true,
                minlength: 8,
                equalTo: "#password"
            },
        },
        messages: {
            "name": {
                required: "'Name' should not be empty.",
                maxlength: "'Name' should not exceeds 50 characters."
            },
            "loginId": {
                required: "'Email' should not be empty.",
                maxlength: "'Email' should not exceeds 50 characters."
            },
            "password": {
                required: "'Password' should not be empty.",
                minlength: "'Password' should be atleast 8 characters.",
            },
            "confirmPassword": {
                required: "'Confirm Password' should not be empty.",
                minlength: "'Confirm Password' should be atleast 8 characters.",
                equalTo: "'Password' and 'Confirm Password' do not match."
            },
        }
    });
}
