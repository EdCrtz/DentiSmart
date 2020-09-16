using System;
using System.Collections.Generic;
using System.Text;

namespace DentiSmart.Infrastructure.DataBase
{
    public class DentiSmartDatabaseSettings : IDentiSmartDatabaseSettings
    {
        public string ConnectionString { get; set; }
        public string ConsultorioCollectionName { get; set; }
        public string DatabaseName { get; set; }
       
    }
    public interface IDentiSmartDatabaseSettings
    {
        public string ConnectionString { get; set; }
        public string ConsultorioCollectionName { get; set; }
        public string DatabaseName { get; set; }
    }
}
