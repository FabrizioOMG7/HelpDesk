var tabla;

function init(){
    $("#categoria_form").on("submit",function(e){
        guardaryeditar(e);
    });
}

function limpiar(){
    $('#codigoCategoria').val('');
    $('#nombreCategoria').val('');
    // Por defecto Activo, y aunque esté disabled el usuario verá "Activo"
    $('#estado').val('true');
}

/* Evento Nuevo Registro */
$(document).on("click","#btnnuevo", function(){
    limpiar();
    $('#mdltitulo').html('Nuevo Registro');
    $('#modalmantenimiento').modal('show');
});

$(document).ready(function(){
    /* Cargar Listado */
    tabla = $('#categoria_data').DataTable({
        "processing": true,
        "serverSide": false,
        dom: 'Bfrtip',
        "searching": true,
        lengthChange: false,
        colReorder: true,
        buttons: ['copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5'],
        "ajax": {
            url: '/api/categorias',
            type: "GET",
            dataType: "json",
            dataSrc: "data",
            error: function(e){
                console.error(e);
            }
        },
        "columns": [
            { "data": "nombreCategoria" },
            {
                "data": "estado",
                "render": function(data) {
                    return data ? '<span class="label label-success">Activo</span>' : '<span class="label label-danger">Inactivo</span>';
                }
            },
            {
                "data": "codigoCategoria",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-warning btn-icon" onClick="editar('+data+')"><i class="fa fa-edit"></i></button>';
                }
            },
            {
                "data": "codigoCategoria",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-danger btn-icon" onClick="eliminar('+data+')"><i class="fa fa-trash"></i></button>';
                }
            },
            {
                // AQUÍ ESTÁ LA LÓGICA DEL CHECK
                "data": null,
                "className": "text-center",
                "render": function(data, type, row) {
                    // Si el estado es false (Inactivo), mostramos el botón verde
                    if (row.estado === false) {
                        return '<button type="button" onClick="activar(' + row.codigoCategoria + ');" class="btn btn-inline btn-success btn-sm" title="Activar"><i class="fa fa-check"></i></button>';
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

    var id = $('#codigoCategoria').val();

    // Aunque el select esté disabled, .val() sigue funcionando en jQuery para leerlo.
    // Si es nuevo registro, por defecto enviamos true.
    var estadoVal = $('#estado').val();

    var datos = {
        codigoCategoria: id,
        nombreCategoria: $('#nombreCategoria').val(),
        // Forzamos true si es nuevo, o leemos el valor si es edición (aunque esté bloqueado)
        estado: (estadoVal === 'true')
    };

    var method = (id === "" || id === null) ? 'POST' : 'PUT';
    var url = (id === "" || id === null) ? '/api/categorias' : '/api/categorias/' + id;

    $.ajax({
        url: url,
        type: method,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(datos),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(datos){
            $('#categoria_form')[0].reset();
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
        url: "/api/categorias/" + id,
        type: "GET",
        success: function(data){
            $('#codigoCategoria').val(data.codigoCategoria);
            $('#nombreCategoria').val(data.nombreCategoria);
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
                url: "/api/categorias/" + id,
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

// NUEVA FUNCIÓN PARA ACTIVAR
function activar(id) {
    swal({
        title: "Confirmar Activación",
        text: "¿Está seguro de reactivar esta categoría?",
        type: "info",
        showCancelButton: true,
        confirmButtonClass: "btn-success",
        confirmButtonText: "Sí, activar",
        cancelButtonText: "No, cancelar",
        closeOnConfirm: false
    },
    function(isConfirm) {
        if (isConfirm) {
            var token = $("input[name='_csrf']").val();
            var header = "X-CSRF-TOKEN";

            $.ajax({
                url: '/api/categorias/' + id + '/activar',
                type: 'PUT',
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data) {
                    tabla.ajax.reload();
                    swal("Activado!", data.message || "La categoría ha sido reactivada.", "success");
                },
                error: function(jqXHR, textStatus, errorThrown) {
                     console.error("Error al activar:", jqXHR.responseText);
                     swal("Error!", "No se pudo activar la categoría.", "error");
                }
            });
        }
    });
}

init();