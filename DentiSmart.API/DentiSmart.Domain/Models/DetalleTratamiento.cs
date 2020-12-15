using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Domain.Models
{
    public class DetalleTratamiento
    {
        public Servicio Servicio { get; set; }
        public string Sintomas { get; set; }
        public string Observacion { get; set; }
        public string Evidencia { get; set; }
        public string Receta { get; set; }
    }
}
