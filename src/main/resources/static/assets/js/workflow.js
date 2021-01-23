function startWorkflowForCustomerOne() {
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    var customerId = $('#customerOne').val();
    $.ajax({
        type: "GET",
        url: BASE_CONTEXT_PATH + "/workflow/startWorkflowForCustomerOne",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#labelMessage").html("");
            $("#labelMessage").html(data.success);
        },
        error: function (e) {
            console.log("ERROR:: " + e);
        }
    });
}
function startWorkflowForCustomerTwo() {
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    var customerId = $('#customerTwo').val();
    $.ajax({
        type: "GET",
        url: BASE_CONTEXT_PATH + "/workflow/startWorkflowForCustomerTwo",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#labelMessage").html("");
            $("#labelMessage").html(data.success);
        },
        error: function (e) {
            console.log("ERROR:: " + e);
        }
    });
}

function startWorkflowForCustomerThree() {
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    var customerId = $('#customerThree').val();
    $.ajax({
        type: "GET",
        url: BASE_CONTEXT_PATH + "/workflow/startWorkflowForCustomerThree",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#labelMessage").html("");
            $("#labelMessage").html(data.success);
        },
        error: function (e) {
            console.log("ERROR:: " + e);
        }
    });
}
function getResponseHistory(selectedCustomerId) {
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    var customerId = $('#'+selectedCustomerId).val();
    $.ajax({
        type: "get",
        contentType: "application/json",
        url: BASE_CONTEXT_PATH + "/workflow/responseHistory",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#salesManMessageToCustomer").val(data.messageToCustomerSB);
            $("#responseHistory").val(data.customerResponseSB);
        },
        error: function (e) {
            console.log("ERROR:: " + e.responseText);
        }
    });
}

// Save Customer Response
function saveResponseMessage() {
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    var customerId = $('#customer').val();
    var response = $('#response').val();
    $.ajax({
        type: "get",
        contentType: "application/json",
        url: BASE_CONTEXT_PATH + "/workflow/saveResponse",
        data: {customerId: customerId, response: response},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#responseHistory").val(data.responseMessage);
            $("#labelMessage").html("");
            $("#labelMessage").html(data.success);
        },
        error: function (e) {
            console.log("ERROR:: " + e.responseText);
        }
    });
}

function viewMessageHistoryForCustomerOne() {
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    var customerId = $('#customerOne').val();
    if(customerId == ""){
        return;
    }
    $.ajax({
        type: "get",
        contentType: "application/json",
        url: BASE_CONTEXT_PATH + "/workflow/responseHistory",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#salesManMessageToCustomer").val(data.messageToCustomerSB);
            $("#responseHistory").val(data.customerResponseSB);
        },
        error: function (e) {
            console.log("ERROR:: " + e.responseText);
        }
    });
}

function viewMessageHistoryForCustomerTwo() {
    var customerId = $('#customerTwo').val();
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    if(customerId == ""){
        return;
    }
    $.ajax({
        type: "get",
        contentType: "application/json",
        url: BASE_CONTEXT_PATH + "/workflow/responseHistory",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#salesManMessageToCustomer").val(data.messageToCustomerSB);
            $("#responseHistory").val(data.customerResponseSB);
        },
        error: function (e) {
            console.log("ERROR:: " + e.responseText);
        }
    });
}

function viewMessageHistoryForCustomerThree() {
    var customerId = $('#customerThree').val();
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    BASE_CONTEXT_PATH = BASE_CONTEXT_PATH.substr(0, BASE_CONTEXT_PATH.length - 1);
    if(customerId == ""){
        return;
    }
    $.ajax({
        type: "get",
        contentType: "application/json",
        url: BASE_CONTEXT_PATH + "/workflow/responseHistory",
        data: {customerId: customerId},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#salesManMessageToCustomer").val(data.messageToCustomerSB);
            $("#responseHistory").val(data.customerResponseSB);
        },
        error: function (e) {
            console.log("ERROR:: " + e.responseText);
        }
    });
}
