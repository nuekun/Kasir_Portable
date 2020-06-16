package com.nuedevlop.kasirportable.utils

import android.content.Context
import android.os.Environment.getExternalStorageDirectory
import android.widget.Toast
import androidx.sqlite.db.SimpleSQLiteQuery
import com.mortgage.fauxiq.pawnbroker.utils.CSVReader
import com.mortgage.fauxiq.pawnbroker.utils.CSVWriter
import com.nuedevlop.kasirportable.utils.database.produk.ProdukDAO
import java.io.*

class CSV(){

    fun exportProduk(produkDAO: ProdukDAO, context:Context) {

        val exportDir = File(getExternalStorageDirectory(), "/kasir")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "produk.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val curCSV = produkDAO.all
            csvWrite.writeNext(curCSV.columnNames)
            while (curCSV.moveToNext()) {
                val arrStr = arrayOfNulls<String>(curCSV.columnCount)
                for (i in 0 until curCSV.columnCount ) {
                    when (i) {
                        20, 22 -> {
                        }
                        else -> arrStr[i] = curCSV.getString(i)
                    }
                }
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()

            Toast.makeText(context,"berhasil",Toast.LENGTH_SHORT).show()
        } catch (sqlEx: Exception) {

        }


    }

    fun importProduk(csvReader:CSVReader,produkDAO: ProdukDAO ,context:Context){

        var nextLine: Array<String> ? = null
        var count = 0
        val columns = StringBuilder()

        try {
            do {
                val value = StringBuilder()
                nextLine = csvReader.readNext()
                nextLine?.let {nextLine->
                    for (i in 0 until nextLine.size ) {
                        if (count == 0) {
                            if (i == nextLine.size - 1) {
                                columns.append(nextLine[i])
                                count =1
                            }
                            else
                                columns.append(nextLine[i]).append(",")
                        } else {
                            if (i == nextLine.size - 1) {
                                value.append("'").append(nextLine[i]).append("'")
                                count = 2
                            }
                            else
                                value.append("'").append(nextLine[i]).append("',")
                        }
                    }
                    if (count==2) {
                        pushProduk(columns, value,produkDAO)
                        Toast.makeText(context,value,Toast.LENGTH_SHORT).show()
                    }
                }
            }while ((nextLine)!=null)

        } catch (e :IOException) {
        }


    }

    private fun pushProduk(columns: StringBuilder, values: Any, produkDAO: ProdukDAO) {


        val query = SimpleSQLiteQuery(
                "INSERT OR REPLACE INTO produk ($columns) values($values)",
                arrayOf()
        )
        produkDAO.insertDataRawFormat(query)
    }


}
