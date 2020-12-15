using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Globalization;
using System.Text;

namespace DentiSmart.Domain.Models
{
    [BsonIgnoreExtraElements]
    public class Usuario
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string Nombre { get; set; }
        public string ApellidoPat { get; set; }
        public string ApellidoMat { get; set; }
        public string NombreUsuario { get; set; }
        public string Contrasenia { get; set; }
        public string Email { get; set; }
        public string Role { get; set; }
        public string Telefono { get; set; }
        #nullable enable
        public string? Foto { get; set; }
        public Expediente? Expediente { get; set; }

        [BsonRepresentation(BsonType.ObjectId)]
        public string? Consultorio {get;set;}
        public Consultorio? DatosConsultorio { get; set; }
        public string? HoraInicio { get; set; }
        public string? HoraFin { get; set; }
        public string? IdFacebook { get; set; }
        public string? IdGoogle { get; set; }
    }
}
