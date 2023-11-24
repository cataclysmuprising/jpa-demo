function init() {
    initValidator();
    loadRoles();
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
    $("#frm-administrator").validate({
        rules: {
            "name": {
                required: true,
                maxlength: 50
            },
            "loginId": {
                required: getPageMode() === 'CREATE',
                maxlength: 50
            },
            "password": {
                required: getPageMode() === 'CREATE',
                minlength: 8
            },
            "confirmPassword": {
                required: getPageMode() === 'CREATE',
                minlength: 8,
                equalTo: "#password"
            },
            "roleIds": {
                required: true,
            },
        },
        messages: {
            "name": {
                required: "'Name' should not be empty.",
                maxlength: "'Name' should not exceeds 50 characters."
            },
            "loginId": {
                required: "'Login ID' should not be empty.",
                maxlength: "'Login ID' should not exceeds 50 characters."
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
            "roleIds": {
                required: "Please select at-least one 'Role'.",
            },
        }
    });
}

function loadRoles() {
    var criteria = {};
    $.ajax({
        type: "POST",
        url: getApiResourcePath() + 'sec/roles/search/list',
        data: JSON.stringify(criteria),
        success: function (data) {
            let options = [];
            $.each(data, function (key, item) {
                let option = "<option value='" + item.id + "'>" + item.name + "</option>";
                if (item.roleType === "built-in") {
                    option = "<option data-type='built-in' value='" + item.id + "' data-subtext='(built-in)'>" + item.name + "</option>";
                }
                options.push(option);
            });
            $("#roleIds").html(options).selectpicker('refresh');
        }
    });

}


