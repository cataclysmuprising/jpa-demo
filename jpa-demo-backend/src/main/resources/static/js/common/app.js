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
let stompClient;
/**
 * ################################## # JS working functions on page ##################################
 */

(function ($) {
    'use strict'

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1)
    }

})(jQuery);
