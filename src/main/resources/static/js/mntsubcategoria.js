var tabla;

function init(){
    $("#subcategoria_form").on("submit",function(e){
        guardaryeditar(e);
    });
    // Cargamos el combo al iniciar
    cargarCategorias();
}

function cargarCategorias(){
    // CAMBIO AQUI: Usamos /api/categorias/combo
    $.get("/api/categorias/combo", function(data) {

        // data ya es una lista [...] porque el endpoint /combo devuelve la lista directa
        var categorias = data;

        $('#cat_id').empty().append('<option value="">Seleccione Categoría</option>');

        $.each(categorias, function(index, value) {
            // Ya no necesitamos validar 'if(value.estado === true)' porque el backend ya lo filtró
            $('#cat_id').append('<option value="' + value.codigoCategoria + '">' + value.nombreCategoria + '</option>');
        });
    }).fail(function(e){
        console.error("Error cargando categorías", e);
    });
}

function limpiar(){
    $('#codigoSubCategoria').val('');
    $('#cat_id').val('');
    $('#nombre').val('');
    $('#estado').val('true');
}

$(document).on("click","#btnnuevo", function(){
    limpiar();
    $('#mdltitulo').html('Nuevo Registro');
    $('#modalmantenimiento').modal('show');
});

$(document).ready(function(){
    tabla = $('#subcategoria_data').DataTable({
        "processing": true,
        "serverSide": false,
        dom: 'Bfrtip',
        buttons: ['copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5'],
        "ajax": {
            url: '/api/subcategorias',
            type: "GET",
            dataType: "json",
            dataSrc: "data",
            error: function(e){ console.error(e); }
        },
        "columns": [
            { "data": "categoria.nombreCategoria" },
            { "data": "nombre" },
            {
                "data": "estado",
                "render": function(data) {
                    return data ? '<span class="label label-success">Activo</span>' : '<span class="label label-danger">Inactivo</span>';
                }
            },
            {
                "data": "codigoSubCategoria",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-warning btn-icon" onClick="editar('+data+')"><i class="fa fa-edit"></i></button>';
                }
            },
            {
                "data": "codigoSubCategoria",
                "className": "text-center",
                "render": function(data) {
                    return '<button type="button" class="btn btn-danger btn-icon" onClick="eliminar('+data+')"><i class="fa fa-trash"></i></button>';
                }
            },
            {
                "data": null,
                "className": "text-center",
                "render": function(data, type, row) {
                    if (row.estado === false) {
                        return '<button type="button" onClick="activar(' + row.codigoSubCategoria + ');" class="btn btn-inline btn-success btn-sm" title="Activar"><i class="fa fa-check"></i></button>';
                    } else {
                        return '';
                    }
                }
            }
        ],
        "language": { "url": "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json" }
    });
});

function guardaryeditar(e){
    e.preventDefault();
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";

    var id = $('#codigoSubCategoria').val();
    var estadoVal = $('#estado').val();
    var catId = $('#cat_id').val();

    var datos = {
        codigoSubCategoria: id,
        nombre: $('#nombre').val(),
        estado: (estadoVal === 'true'),
        categoria: {
            codigoCategoria: parseInt(catId)
        }
    };

    var method = (id === "" || id === null) ? 'POST' : 'PUT';
    var url = (id === "" || id === null) ? '/api/subcategorias' : '/api/subcategorias/' + id;

    $.ajax({
        url: url,
        type: method,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(datos),
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(datos){
            $('#subcategoria_form')[0].reset();
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
            var msg = "Error al guardar";
            if(xhr.responseJSON && xhr.responseJSON.error) {
                msg = xhr.responseJSON.error;
            }
            swal("Error", msg, "error");
        }
    });
}

function editar(id){
    $('#mdltitulo').html('Editar Registro');
    $.ajax({
        url: "/api/subcategorias/" + id,
        type: "GET",
        success: function(data){
            $('#codigoSubCategoria').val(data.codigoSubCategoria);
            $('#nombre').val(data.nombre);
            $('#estado').val(data.estado.toString());

            if(data.categoria) {
                $('#cat_id').val(data.categoria.codigoCategoria);
            }

            $('#modalmantenimiento').modal('show');
        }
    });
}

function eliminar(id){
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";
    swal({
        title: "HelpDesk",
        text: "¿Está seguro de eliminar (desactivar)?",
        type: "error",
        showCancelButton: true,
        confirmButtonClass: "btn-danger",
        confirmButtonText: "Si",
        closeOnConfirm: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.ajax({
                url: "/api/subcategorias/" + id,
                type: "DELETE",
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data){
                    tabla.ajax.reload();
                    swal("Eliminado!", "Registro desactivado.", "success");
                },
                error: function(e) { swal("Error", "No se pudo eliminar", "error"); }
            });
        }
    });
}

function activar(id) {
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";
    swal({
        title: "Confirmar Activación",
        text: "¿Reactivar registro?",
        type: "info",
        showCancelButton: true,
        confirmButtonClass: "btn-success",
        confirmButtonText: "Sí",
        closeOnConfirm: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.ajax({
                url: '/api/subcategorias/' + id + '/activar',
                type: 'PUT',
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data) {
                    tabla.ajax.reload();
                    swal("Activado!", "Registro reactivado.", "success");
                },
                error: function(e) { swal("Error", "No se pudo activar", "error"); }
            });
        }
    });
}

init();