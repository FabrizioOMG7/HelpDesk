var tabla;

function init(){
    $("#prioridad_form").on("submit",function(e){
        guardaryeditar(e);
    });
}

function limpiar(){
    $('#codigoPrioridad').val('');
    $('#nombrePrioridad').val('');
    $('#estado').val('true');
}

$(document).on("click","#btnnuevo", function(){
    limpiar();
    $('#mdltitulo').html('Nuevo Registro');
    $('#modalmantenimiento').modal('show');
});

$(document).ready(function(){
    tabla = $('#prioridad_data').DataTable({
        "processing": true,
        "serverSide": false,
        dom: 'Bfrtip',
        "searching": true,
        lengthChange: false,
        colReorder: true,
        buttons: ['copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5'],
        "ajax": {
            url: '/api/prioridades',
            type: "GET",
            dataType: "json",
            dataSrc: "data",
            error: function(e){
                console.error(e);
            }
        },
        "columns": [
            { "data": "nombrePrioridad" },
            {
                "data": "estado",
                "render": function(data) {
                    return data ? '<span class="label label-success">Activo</span>' : '<span class="label label-danger">Inactivo</span>';
                }
            },
            {
                "data": "codigoPrioridad",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-warning btn-icon" onClick="editar('+data+')"><i class="fa fa-edit"></i></button>';
                }
            },
            {
                "data": "codigoPrioridad",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-danger btn-icon" onClick="eliminar('+data+')"><i class="fa fa-trash"></i></button>';
                }
            },
            {
                // Logica del boton Activar
                "data": null,
                "className": "text-center",
                "render": function(data, type, row) {
                    if (row.estado === false) {
                        return '<button type="button" onClick="activar(' + row.codigoPrioridad + ');" class="btn btn-inline btn-success btn-sm" title="Activar"><i class="fa fa-check"></i></button>';
                    } else {
                        return '';
                    }
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

    var id = $('#codigoPrioridad').val();
    var estadoVal = $('#estado').val();

    var datos = {
        codigoPrioridad: id,
        nombrePrioridad: $('#nombrePrioridad').val(),
        estado: (estadoVal === 'true')
    };

    var method = (id === "" || id === null) ? 'POST' : 'PUT';
    var url = (id === "" || id === null) ? '/api/prioridades' : '/api/prioridades/' + id;

    $.ajax({
        url: url,
        type: method,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(datos),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(datos){
            $('#prioridad_form')[0].reset();
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
        url: "/api/prioridades/" + id,
        type: "GET",
        success: function(data){
            $('#codigoPrioridad').val(data.codigoPrioridad);
            $('#nombrePrioridad').val(data.nombrePrioridad);
            $('#estado').val(data.estado.toString());
            $('#modalmantenimiento').modal('show');
        }
    });
}

function eliminar(id){
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";

    swal({
        title: "HelpDesk",
        text: "¿Está seguro de eliminar (desactivar) el registro?",
        type: "error",
        showCancelButton: true,
        confirmButtonClass: "btn-danger",
        confirmButtonText: "Si",
        cancelButtonText: "No",
        closeOnConfirm: false
    },
    function(isConfirm) {
        if (isConfirm) {
            $.ajax({
                url: "/api/prioridades/" + id,
                type: "DELETE",
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data){
                    tabla.ajax.reload();
                    swal({
                        title: "HelpDesk!",
                        text: "Registro Eliminado.",
                        type: "success",
                        confirmButtonClass: "btn-success"
                    });
                }
            });
        }
    });
}

function activar(id) {
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";

    swal({
        title: "Confirmar Activación",
        text: "¿Está seguro de reactivar esta prioridad?",
        type: "info",
        showCancelButton: true,
        confirmButtonClass: "btn-success",
        confirmButtonText: "Sí, activar",
        cancelButtonText: "No, cancelar",
        closeOnConfirm: false
    },
    function(isConfirm) {
        if (isConfirm) {
            $.ajax({
                url: '/api/prioridades/' + id + '/activar',
                type: 'PUT',
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data) {
                    tabla.ajax.reload();
                    swal("Activado!", data.message || "La prioridad ha sido reactivada.", "success");
                },
                error: function(jqXHR, textStatus, errorThrown) {
                     console.error("Error al activar:", jqXHR.responseText);
                     swal("Error!", "No se pudo activar la prioridad.", "error");
                }
            });
        }
    });
}

init();