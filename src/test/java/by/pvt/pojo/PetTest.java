package by.pvt.pojo;

import by.pvt.util.DBUnitTestBase;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class PetTest extends DBUnitTestBase {

    public PetTest(String name) {
        super(name);
    }

    @Test
    public void testSavePet() throws Exception {
        Callable<Pet> savePet = () -> {
            Pet p = createTestData(7, "Tom", "Cat", new Date());
            session.saveOrUpdate(p);
            return p;
        };

        Pet p = executeInTransaction(savePet);

        Callable<List<Pet>> getPetList = () -> session.createQuery("from pet").list();

        List<Pet> petList = executeInTransaction(getPetList);

        Assert.assertEquals(6, petList.size());
        Pet p2 = petList.get(5);
        Assert.assertEquals(p, p2);
    }

    @Test
    public void testGetListPet() throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("pet");

        Callable<Integer> getListPet = () -> {
            List<Pet> petList =
                    session.createQuery("from pet").list();
            int count = petList.size();
            System.out.println(count);
            return count;
        };

        Integer count = executeInTransaction(getListPet);

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(PetTest.class.getResourceAsStream("pet_TestDataSet_Expected.xml"));
        ITable expectedTable = expectedDataSet.getTable("pet");
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredActualTable);
        Assert.assertEquals(5, count.longValue());
    }

    @Test
    public void testUpdatePet() throws Exception {
        Callable<Pet> updatePet = () -> {
            Pet pet = createTestData(5, "Jerry", "Mouse", new Date());
            session.saveOrUpdate(pet);
            return pet;
        };

        Pet pet = executeInTransaction(updatePet);

        Callable<Pet> selectPet = () -> {
            Pet loadedPet = session.get(Pet.class, 5);
            return loadedPet;
        };

        Pet loadedPet = executeInTransaction(selectPet);

        Assert.assertEquals(pet.getId(), loadedPet.getId());
        Assert.assertEquals(pet, loadedPet);
    }

    @Test
    public void testDeletePet() throws Exception {
        int petToDelete = 5;

        Callable deletePet = () -> {
            Pet loadedPet = session.get(Pet.class, petToDelete);
            session.delete(loadedPet);
            return null;
        };

        executeInTransaction(deletePet);

        List<Pet> petList =
                session.createQuery("from pet").list();
        Assert.assertEquals(4, petList.size());
        assertTrue(petList.stream().noneMatch(pet -> pet.getId() == petToDelete));
    }

    private static Pet createTestData(int id, String name, String animal, Date birthDay) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setAnimal(animal);
        pet.setDateOfBirth(birthDay);
        pet.setGender('m');
        pet.setVaccination(true);
        return pet;
    }

    @Override
    protected String getResourceName() {
        return "pet_TestDataSet.xml";
    }
}