var tabla;

function init(){
    $("#area_form").on("submit",function(e){
        guardaryeditar(e);
    });
}

function limpiar(){
    $('#codigoArea').val('');
    $('#nombreArea').val('');
}

/* Evento Nuevo Registro */
$(document).on("click","#btnnuevo", function(){
    limpiar();
    $('#mdltitulo').html('Nuevo Registro');
    $('#modalmantenimiento').modal('show');
});

$(document).ready(function(){
    /* Cargar Listado */
    tabla = $('#area_data').DataTable({
        "processing": true,
        "serverSide": false,
        dom: 'Bfrtip',
        "searching": true,
        lengthChange: false,
        colReorder: true,
        buttons: ['copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5'],
        "ajax": {
            url: '/api/areas',
            type: "GET",
            dataType: "json",
            dataSrc: "data",
            error: function(e){
                console.error(e);
            }
        },
        "columns": [
            { "data": "nombreArea" },
            {
                "data": "codigoArea",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-warning btn-icon" onClick="editar('+data+')"><i class="fa fa-edit"></i></button>';
                }
            }
        ],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json"
        }
    });
});

function guardaryeditar(e){
    e.preventDefault();

    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";

    var id = $('#codigoArea').val();

    var datos = {
        codigoArea: id,
        nombreArea: $('#nombreArea').val()
    };

    var method = (id === "" || id === null) ? 'POST' : 'PUT';
    var url = (id === "" || id === null) ? '/api/areas' : '/api/areas/' + id;

    $.ajax({
        url: url,
        type: method,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(datos),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(datos){
            $('#area_form')[0].reset();
            $("#modalmantenimiento").modal('hide');
            tabla.ajax.reload();

            swal({
                title: "HelpDesk!",
                text: id ? "Actualizado Correctamente" : "Registrado Correctamente",
                type: "success",
                confirmButtonClass: "btn-success"
            });
        },
        error: function(xhr) {
            console.log(xhr.responseText);
            swal("Error", "No se pudo guardar.", "error");
        }
    });
}

function editar(id){
    $('#mdltitulo').html('Editar Registro');

    $.ajax({
        url: "/api/areas/" + id,
        type: "GET",
        success: function(data){
            $('#codigoArea').val(data.codigoArea);
            $('#nombreArea').val(data.nombreArea);
            $('#modalmantenimiento').modal('show');
        }
    });
}

init();