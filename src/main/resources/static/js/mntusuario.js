var tabla;

function init(){
    $("#usuario_form").on("submit",function(e){
        guardaryeditar(e);
    });
    cargarCombos();
}

function cargarCombos(){
    //ComboBox roles
    $.get("/api/roles", function(data) {
    //Solo roles activos
        var rolesActivos = data.filter(function(rol){ return rol.estado === true; });
        $('#rolId').empty().append('<option value="">Seleccionar Rol</option>');
        $.each(rolesActivos, function(index, value) {
            $('#rolId').append('<option value="' + value.codigoRol + '">' + value.nombreRol + '</option>');
        });
        $('#rolId').select2({dropdownParent: $('#modalmantenimiento')});
    });

    // ComboBox Areas (CORREGIDO)
    $.get("/api/areas/combo", function(data) {
        $('#areaId').empty().append('<option value="">Seleccionar Área</option>');
        $.each(data, function(index, value) {
            $('#areaId').append('<option value="' + value.codigoArea + '">' + value.nombreArea + '</option>');
        });
        $('#areaId').select2({dropdownParent: $('#modalmantenimiento')});
    });
}


function limpiar(){
    $('#usu_id').val('');
    $('#nombres').val('');
    $('#apellidoPaterno').val('');
    $('#apellidoMaterno').val('');
    $('#correo').val('');
    $('#passwoord').val('').attr("placeholder", "Ingrese Contraseña");
    $('#rolId').val('').trigger('change');
    $('#areaId').val('').trigger('change');
    $('#passwoord').prop('required', true);
}


function mostrarModal(flag){ // flag=true -> Nuevo Registro, flag=false -> Editar Registro
    limpiar();
    if (flag){
        $('#mdltitulo').html('Nuevo Usuario');
        $('#passwoord').prop('required', true).attr("placeholder", "Ingrese Contraseña");
        $('#modalmantenimiento').modal('show');
    } else {
        $('#mdltitulo').html('Editar Usuario');
        $('#passwoord').prop('required', false).attr("placeholder", "Dejar vacío para no cambiar");
        $('#modalmantenimiento').modal('show');
    }
}

$('#btnnuevo').click(function(){
    mostrarModal(true);
});

$(document).ready(function(){
    // Inicializar DataTable
    tabla = $('#usuario_data').DataTable({
        "processing": true,
        "serverSide": false,
        "ajax":{
            url: '/api/usuarios',
            type : "get",
            dataType : "json",
            dataSrc: "data",
            error: function(e){
                console.error("Error cargando datos:", e.responseText);
                swal("Error!", "No se pudieron cargar los datos de usuarios.", "error");
            }
        },
        dom: 'Bfrtip',
        buttons: [
                    'copyHtml5',
                    'excelHtml5',
                    'csvHtml5',
                    'pdf'
                ],
        "responsive": true,
        "bInfo":true,
        "iDisplayLength": 10,
        "order": [[ 0, "asc" ]],
        "language": { /* ... */ },

        "columns": [
            { "data": "nombres" },
            { "data": "apellidoPaterno" },
            { "data": "apellidoMaterno" },
            { "data": "correo", "className": "d-none d-sm-table-cell" },
            { "data": "rol.nombreRol", "className": "d-none d-sm-table-cell" },
            { "data": "area.nombreArea", "className": "d-none d-sm-table-cell" },
            { // Fecha Modificacion
                "data": "fechaModificacion",
                "render": function(data, type, row) {
                    if (data) {
                         try { return new Date(data).toLocaleDateString() + ' ' + new Date(data).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); }
                         catch (e) { return "Fecha inválida"; }
                    } else { return '<span class="label label-default">Sin Fecha</span>'; }
                },
                "className": "d-none d-sm-table-cell"
            },
            { // Fecha Eliminacion
                "data": "fechaEliminacion",
                "render": function(data, type, row) {
                    if (data) {
                         try { return new Date(data).toLocaleDateString() + ' ' + new Date(data).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); }
                         catch (e) { return "Fecha inválida"; }
                    } else { return '<span class="label label-default">Sin Fecha</span>'; }
                },
                "className": "d-none d-sm-table-cell"
            },
            {
                "data": "estado",
                "className": "d-none d-sm-table-cell text-center",
                "render": function(data, type, row) {
                    if (data === true) {
                        return '<span class="label label-success">Activo</span>';
                    } else {
                        return '<span class="label label-danger">Inactivo</span>';
                    }
                }
            },
            {
                "data": null,
                "orderable": false,
                "className": "text-center",
                "render": function (data, type, row) {
                    return '<button type="button" class="btn btn-inline btn-warning btn-sm" onClick="editar('+row.codigoUsuario+')"><i class="fa fa-pencil"></i></button>';
            }},
            {
                "data": null,
                "orderable": false,
                "className": "text-center",
                "render": function (data, type, row) {
                    return '<button type="button" class="btn btn-inline btn-danger btn-sm" onClick="eliminar('+row.codigoUsuario+')"><i class="fa fa-trash"></i></button>';
            }},
            {
                "data": null,
                "orderable": false,
                "className": "text-center",
                "render": function(data, type, row) {
                    if (row.estado === false) { // Muestra botón solo si está inactivo
                        return '<button type="button" onClick="activarUsuario(' + row.codigoUsuario + ');" class="btn btn-inline btn-success btn-sm" title="Activar Usuario"><i class="fa fa-check"></i></button>';
                    } else {
                        return '';
                    }
                }
            }
        ]
    });
});



function guardaryeditar(e){
    e.preventDefault();
    var usuId = $('#usu_id').val();
    var url = (usuId) ? '/api/usuarios/' + usuId : '/api/usuarios';
    var method = (usuId) ? 'PUT' : 'POST';


    var usuarioJson = {
        nombres: $('#nombres').val(),
        apellidoPaterno: $('#apellidoPaterno').val(),
        apellidoMaterno: $('#apellidoMaterno').val(),
        correo: $('#correo').val(),
        passwoord: $('#passwoord').val() || null,
        rol: { codigoRol: $('#rolId').val() ? parseInt($('#rolId').val()) : null },
        area: { codigoArea: $('#areaId').val() ? parseInt($('#areaId').val()) : null }
    };

    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";

    $.ajax({
        url: url,
        type: method,
        data: JSON.stringify(usuarioJson),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(data){
            $('#usuario_form')[0].reset();
            $("#modalmantenimiento").modal('hide');
            tabla.ajax.reload();

            swal("Correcto!", (usuId ? "Actualizado" : "Registrado") + " Correctamente", "success");
        },
        error: function(jqXHR, textStatus, errorThrown){
             console.error("Error:", textStatus, errorThrown);
             console.error("Response:", jqXHR.responseText);
             var errorMsg = "Hubo un problema al guardar.";
             if (jqXHR.responseJSON && jqXHR.responseJSON.error) {
                 errorMsg = jqXHR.responseJSON.error;
             } else if (jqXHR.responseText) {
                 try { errorMsg = $(jqXHR.responseText).text() || jqXHR.responseText; } catch(e){}
             }
             swal("Error!", errorMsg, "error");
        }
    });
}


function editar(usu_id){
    $.get("/api/usuarios/" + usu_id, function(data) {
        $('#mdltitulo').html('Editar Usuario');
        $('#usu_id').val(data.codigoUsuario);
        $('#nombres').val(data.nombres);
        $('#apellidoPaterno').val(data.apellidoPaterno);
        $('#apellidoMaterno').val(data.apellidoMaterno);
        $('#correo').val(data.correo);
        $('#passwoord').val('').attr("placeholder", "Dejar vacío para no cambiar").prop('required', false);
        $('#rolId').val(data.rol ? data.rol.codigoRol : '').trigger('change');
        $('#areaId').val(data.area ? data.area.codigoArea : '').trigger('change');
        $('#modalmantenimiento').modal('show');
    }).fail(function(jqXHR) {
         console.error("Error al cargar usuario:", jqXHR.responseText);
         swal("Error!", "No se pudo cargar la información del usuario.", "error");
    });
}


function eliminar(usu_id){
    swal({
        title: "Confirmar Eliminación",
        text: "¿Está seguro de Eliminar (desactivar) este usuario?",
        type: "warning",
        showCancelButton: true,
        confirmButtonClass: "btn-danger",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "No, cancelar",
        closeOnConfirm: false
    },
    function(isConfirm) {
        if (isConfirm) {
             var token = $("input[name='_csrf']").val();
             var header = "X-CSRF-TOKEN";

            $.ajax({
                url: "/api/usuarios/" + usu_id,
                type: "DELETE",
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function (datos) {
                    swal("Eliminado!", datos.message || "El usuario ha sido desactivado.", "success");
                    tabla.ajax.reload();
                },
                error: function(jqXHR, textStatus, errorThrown){
                     console.error("Error al eliminar:", jqXHR.responseJSON || textStatus);
                     var errorMsg = "No se pudo eliminar el usuario.";
                     if(jqXHR.responseJSON && jqXHR.responseJSON.error) {
                         errorMsg = jqXHR.responseJSON.error;
                     }
                     swal("Error!", errorMsg, "error");
                }
            });
        }
    });
}

function activarUsuario(usu_id) {
    swal({
        title: "Confirmar Activación",
        text: "¿Está seguro de reactivar este usuario?",
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
                url: '/api/usuarios/' + usu_id + '/activar',
                type: 'PUT',
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data) {
                    tabla.ajax.reload();
                    swal("Activado!", data.message || "El usuario ha sido reactivado.", "success");
                },
                error: function(jqXHR, textStatus, errorThrown) {
                     console.error("Error al activar:", jqXHR.responseJSON || textStatus);
                     var errorMsg = "No se pudo activar el usuario.";
                     if(jqXHR.responseJSON && jqXHR.responseJSON.error) {
                         errorMsg = jqXHR.responseJSON.error;
                     }
                     swal("Error!", errorMsg, "error");
                }
            });
        }
    });
}
init();
