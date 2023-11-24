let dataTable;

function init() {
    initDataTable();
}

function bind() {

    $("#btnSearch").on('click', function (e) {
        dataTable.search($(this).val()).draw();
    });

    $("#btnReset").on('click', function (e) {
        $('form').trigger('reset');
        dataTable.search($(this).val()).draw();
    });

}

function initDataTable() {
    let columns = [{
        "mData": "name",
    }
        , {
            "render": function (data, type, full, meta) {
                if (full.status) {
                    if (full.status === "ACTIVE") {
                        return '<i class="fa fa-check text-green"></i>';
                    }
                    else if (full.status === "TEMPORARY") {
                        return '<i class="fa fa-exclamation-triangle text-yellow"></i>';
                    }
                    else if (full.status === "LOCKED") {
                        return '<i class="fa fa-lock text-red"></i>';
                    }
                    else {
                        return '-';
                    }

                }
                else {
                    return '-';
                }

            },
            "sClass": "text-center",
        },];
    if (hasAuthority("adminDetail") || hasAuthority("adminEdit") || hasAuthority("adminRemove")) {
        columns.push({
            "render": function (data, type, full, meta) {
                let detailButton = {
                    label: "View",
                    authorityName: "adminDetail",
                    url: getContextPath() + "/sec/admins/" + full.id,
                    styleClass: "",
                    data_id: full.id
                };
                let editButton = {
                    label: "Edit",
                    authorityName: "adminEdit",
                    url: getContextPath() + "/sec/admins/" + full.id + '/edit',
                    styleClass: "",
                    data_id: full.id
                };
                let removeButton = {
                    label: "Remove",
                    authorityName: "adminRemove",
                    url: getContextPath() + "/sec/admins/" + full.id + '/delete',
                    styleClass: "remove",
                    data_id: full.id
                };
                return generateAuthorizedButtonGroup([detailButton, editButton, removeButton]);
            },
            "bSortable": false,
            "sClass": "text-center"
        });
    }
    dataTable = $('#tblAdministrator').DataTable({
        aoColumns: columns,
        "aaSorting": [],
        columnDefs: [{
            width: 100,
            targets: 2
        }],
        ajax: {
            type: "POST",
            url: getApiResourcePath() + 'sec/admins/search/paging',
            data: function (d) {
                let criteria = {};
                if (d.order.length > 0) {
                    let index = $(d.order[0])[0].column;
                    let dir = $(d.order[0])[0].dir;
                    let head = $("#tblAdministrator").find("thead");
                    let sortColumn = head.find("th:eq(" + index + ")");
                    criteria.sortType = dir.toUpperCase();
                    criteria.sortProperty = $(sortColumn).attr("data-sort-key");
                }
                criteria.offset = d.start;
                criteria.limit = d.length;
                let word = $("#keyword").val();
                if (isNotEmpty(word)) {
                    criteria.keyword = word.trim();
                }
                return JSON.stringify(criteria);
            }
        },
        initComplete: function () {
            let api = this.api();
            $('#keyword').off('.DT').on('keyup.DT', function (e) {
                if (e.keyCode === 13) {
                    api.search(this.value).draw();
                }
            });
        },
        drawCallback: function (settings) {
            bindRemoveButtonEvent();
        }
    });
}
