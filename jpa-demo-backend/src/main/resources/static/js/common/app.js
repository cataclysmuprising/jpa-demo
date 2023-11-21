/**
 * @file app.js
 * @description This js file initiate all global settings for website. This file
 *              may include in most of pages.
 * @version 3.0
 * @author Than Htike Aung
 * @contact thanhtikeaung@tamantaw.com
 * @copyright Copyright (c) 2017-2018, Than Htike Aung. This source file is free
 *            software, available under the following license: MIT license. See
 *            the license file for details.
 */

/**
 * ################################## # Global Variables ##################################
 */
let ROW_PER_PAGE = 20;
let SECONDARY_ROW_PER_PAGE = 10;
let FILE_SIZE_UNITS = ['Bytes', 'KB', 'MB', 'GB'];
let PAGE_MODE = "";
/**
 * ################################## # JS working functions on page ##################################
 */
$.ajaxSetup({
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    timeout: 100000,
});
$(document).ajaxSend((e, xhr, options) => {
    let $csrf_token = $("meta[name='_csrf']").attr("content");
    let $csrf_header = $("meta[name='_csrf_header']").attr("content");
    if ($csrf_token && $csrf_header) {
        xhr.setRequestHeader($csrf_header, $csrf_token);
    }
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    xhr.setRequestHeader('Accept', 'application/json');
}).ajaxComplete((event, xhr, settings) => {
    // if (xhr.status === 208 || xhr.status === 226) {
    //     document.write(xhr.responseText);
    // }
}).ajaxError((event, xhr, settings, thrownError) => {
    notify("Error !", "Something went wrong with requesting data to server.", "error");
    console.log(xhr);
});

/**
 * ################################## # Application's Main Functions ##################################
 */
$(function () {
    PAGE_MODE = $('#pageMode').val();
    baseInit();
    baseBind();
    if (typeof init === "function") {
        init();
    }
    if (typeof bind === "function") {
        bind();
    }
});

function baseInit() {
    $('.disabled').attr('tabindex', '-1');
    $('.disabled input').attr('tabindex', '-1');
    $('form,input').attr('autocomplete', 'off');
    $('form').trigger("reset");
    initJQueryDataTable();
    initSelectPickers();
    initLobiboxSettings();
    initJQueryValidator();
    loadValidationErrors();
    initPageMessage();
}

function baseBind() {
    $('form').on('reset', function (e) {
        setTimeout(function () {
            let selectpicker = $(".selectpicker");
            selectpicker.selectpicker('refresh');
            selectpicker.trigger('refreshed.bs.select');
        });
    });

    disableFormSubmitEvent();
}

/**
 * ################################## # Application's Initializings Functions ##################################
 */

function initPageMessage() {
    if ($("#pageMessage").length > 0) {
        let pageMessage = $("#pageMessage");
        notify($(pageMessage).attr("data-title"), $(pageMessage).attr("data-info"), $(pageMessage).attr("data-style"));
    }
}

function initJQueryDataTable() {
    if ($.fn.DataTable) {
        $.extend(true, $.fn.dataTable.defaults, {
            "lengthChange": false,
            "searching": false,
            pagingType: "first_last_numbers",
            "pageLength": ROW_PER_PAGE,
            processing: true,
            serverSide: true,
            autoWidth: false,
            aaSorting: [],
            "language": {
                "zeroRecords": "No matching records found.",
                'loadingRecords': '&nbsp;',
            },
            infoCallback: function (roles, start, end, max, total, pre) {
                if (total > 0) {
                    return "Showing " + start + " to " + end + " of " + total + " entries";
                }
                else {
                    return "No Entry to Show";
                }
            }
        });

        $(window).resize(function () {
            if (this.resizeTO) {
                clearTimeout(this.resizeTO);
            }
            this.resizeTO = setTimeout(function () {
                $(this).trigger('resizeEnd');
            }, 500);
        });

        $(window).bind('resizeEnd', function () {
            $(".datatable_scrollArea").scroll();
        });
    }
}

function initSelectPickers() {
    // set selected value for select picker
    $(".selectpicker").on('refreshed.bs.select', function (e) {
        let _selected = $(this).attr("data-selected");
        if (_selected && _selected.trim().length > 0) {
            // for multiple select picker
            if ($(this).attr("multiple")) {
                _selected = _selected.replaceSome("]", "[", " ", "");
                $(this).selectpicker('val', _selected.split(","));
            }

            else {
                $(this).selectpicker('val', _selected).change();
            }
        }

    });
}

function initLobiboxSettings() {
    if (typeof Lobibox !== 'undefined' && typeof Lobibox.notify === "function") {
        Lobibox.notify.DEFAULTS = $.extend({}, Lobibox.notify.DEFAULTS, {
            size: 'mini',
            iconSource: "fontAwesome",
            showClass: 'zoomIn',
            hideClass: 'lightSpeedOut',
            continueDelayOnInactiveTab: true,
            pauseDelayOnHover: true,
            sound: false,
            delay: 10000,
            img: getStaticResourcePath() + "/images/logo.png",
            warning: {
                title: 'Warning',
                iconClass: 'fa fa-exclamation-circle'
            },
            info: {
                title: 'Information',
                iconClass: 'fa fa-info-circle'
            },
            success: {
                title: 'Success',
                iconClass: 'fa fa-check-circle'
            },
            error: {
                title: 'Error',
                iconClass: 'fa fa-times-circle'
            }
        });
    }
}

function initJQueryValidator() {
    if ($.fn.validate) {
        $.validator.setDefaults({
            errorElement: "div",
            errorClass: "invalid-feedback",
            ignore: [],
            errorPlacement: function (error, element) {
                let container = element;
                if (element.hasClass('selectpicker')) {
                    container = element.closest('.bootstrap-select');
                }
                else if (element.closest('.input-group').length > 0) {
                    container = element.closest('.input-group');
                }
                error.insertAfter(container);
            },
            highlight: function (element) {
                let container = $(element);
                if ($(element).hasClass('selectpicker')) {
                    container = $(element).closest('.bootstrap-select');
                }
                else if ($(element).closest('.input-group').length > 0) {
                    container = $(element).closest('.input-group');
                }
                container.addClass("is-invalid");
            },
            unhighlight: function (element) {
                let container = $(element);
                if ($(element).hasClass('selectpicker')) {
                    container = $(element).closest('.bootstrap-select');
                }
                else if ($(element).closest('.input-group').length > 0) {
                    container = $(element).closest('.input-group');
                }
                container.removeClass("is-invalid");
            },
            success: function (error) {
                $(error).remove();
            },
            onkeyup: function () {
                return false;
            },
            onfocusout: function () {
                return false;
            }
        });
    }
}

/**
 * ############################ # Base event binding Functions ############################
 */

function disableFormSubmitEvent() {
    $('form').on('keyup keypress', function (e) {
        let keyCode = e.keyCode || e.which;
        if (e.target.localName !== 'textarea' && keyCode === 13) {
            e.preventDefault();
            return false;
        }
    });
}

function bindRemoveButtonEvent(selector) {

    if (!isNotEmpty(selector)) {
        selector = ".remove";
    }

    $(selector).on("click", function (e) {
        e.preventDefault();
        let url = $(this).attr("href");
        $("#deleteConfirmModal").modal({
            backdrop: 'static',
            keyboard: false
        });
        $("#confirmDelete").off('click').on('click', function (e) {
            $("#deleteConfirmModal").modal("hide");
            window.location.href = url;
        });
    });
}

/**
 * ################################## # Global Functions ##################################
 */

function clearOldValidationErrorMessages() {
    $(".invalid-feedback").remove();
    $('.form-control').removeClass('is-invalid');
}

function loadValidationErrors() {
    let errorElems = $("#validationErrors .error-item");
    if (errorElems.length > 0) {
        clearOldValidationErrorMessages();
        $.each(errorElems, function (index, item) {
            let elementId = $(item).attr("data-id").replace(new RegExp("\\.", "g"), '_');
            let errorElem = $("#" + elementId);
            let container = errorElem;
            if (errorElem.hasClass('selectpicker')) {
                container = errorElem.closest('.bootstrap-select');
            }
            if (errorElem.length) {
                container.addClass('is-invalid');
                container.after('<div class="invalid-feedback">' + $(item).attr("data-error-message") + '</div>');
            }
        });
    }
}

function notify(title, message, style) {
    if (typeof Lobibox !== 'undefined' && typeof Lobibox.notify === "function") {
        if ($(".lobibox-notify-wrapper").length > 0) {
            $(".lobibox-notify-wrapper").remove();
        }
        Lobibox.notify(style, {
            msg: message,
            title: title
        });
    }
}

function handleServerResponse(response) {
    if (response.status === "METHOD_NOT_ALLOWED") {
        if (response.type === "validationError") {
            $("#validationErrors").empty();
            $.each(response.fieldErrors, (key, value) => {
                $("#validationErrors").append('<span class="error-item" data-id="' + key + '" data-error-message="' + value + '" />');
            });
            loadValidationErrors();
        }
    }
    else if (response.status === "OK") {
        $(".modal").modal("hide");
        clearOldValidationErrorMessages();
    }
    if (response.pageMessage) {
        let pageMessage = response.pageMessage;
        notify(pageMessage.title, pageMessage.message, pageMessage.style);
    }
}

function getLocalStorageItem(name) {
    if (typeof (Storage) !== 'undefined') {
        return localStorage.getItem(name);
    }
    else {
        window.alert('Please use a modern browser to properly view this template!');
    }
}

function saveInLocalStorage(name, val) {
    if (typeof (Storage) !== 'undefined') {
        localStorage.setItem(name, val);
    }
    else {
        window.alert('Please use a modern browser to properly view this template!');
    }
}

function removeFromLocalStorage(name) {
    if (typeof (Storage) !== 'undefined') {
        localStorage.removeItem(name);
    }
    else {
        window.alert('Please use a modern browser to properly view this template!');
    }
}

function goToHomePage() {
    if ($(".breadcrumb > li > a")[1]) {
        $(".breadcrumb > li > a")[1].click();
    }
    else {
        $(".breadcrumb > li > a")[0].click();
    }
}

function reloadCurrentPage() {
    location.reload(true);
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

function getApiResourcePath() {
    return getContextPath() + "/api/";
}

function getStaticResourcePath() {
    return $("#baseStaticRssDir").val();
}

function getPageMode() {
    return $("#pageMode").val();
}

function hasAuthority(actionName) {
    return $("#" + actionName).val() === "true";
}

function convertJSONValueToCommaSeparateString(acceptanceElemSelector) {
    try {
        let json = JSON.parse($(acceptanceElemSelector).val());
        $(acceptanceElemSelector).val(json);
    }
    catch (exception) {
    }
}

function removeElementByIndex(arr, x) {
    let newArr = [];
    for (let i = 0; i < arr.length; i++) {
        if (i !== x) {
            newArr.push(arr[i]);
        }
    }
    return newArr;
}

function formatNumber(x) {
    if (x !== null && x !== undefined) {
        return x.toLocaleString(undefined, {maximumFractionDigits: 2});
    }
    return "-";
}

function readCookie(name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) === 0) {
            return c.substring(nameEQ.length, c.length);
        }
    }
    return null;
}

function isNotEmpty(obj) {
    if (null == obj) {
        return false;
    }
    else if (obj.toString() !== "") {
        return true;
    }
    return false;
}

function post_to_url(path, params, target, method) {
    method = method || "post";

    let form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    form.setAttribute("target", target);

    let $csrf_token = $("meta[name='_csrf']").attr("content");
    let hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "_csrf");
    hiddenField.setAttribute("value", $csrf_token);
    form.appendChild(hiddenField);

    for (let key in params) {
        if (params.hasOwnProperty(key)) {
            let hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);
            form.appendChild(hiddenField);
        }
    }

    document.body.appendChild(form);
    form.submit();
}

/**
 * ####################################### # JavaScript utility methods for Array #######################################
 */

function convertUniqueArray(a) {
    let temp = {};
    for (let i = 0; i < a.length; i++) {
        temp[a[i]] = true;
    }
    let r = [];
    for (let k in temp)
        r.push(k);
    return r;
}

function findWithAttr(array, attr, value) {
    for (let i = 0; i < array.length; i += 1) {
        if (array[i][attr] === value) {
            return i;
        }
    }
    return -1;
}

String.prototype.replaceSome = function () {
    var replaceWith = Array.prototype.pop.apply(arguments), i = 0, r = this, l = arguments.length;
    for (; i < l; i++) {
        r = r.replace(arguments[i], replaceWith);
    }
    return r;
};