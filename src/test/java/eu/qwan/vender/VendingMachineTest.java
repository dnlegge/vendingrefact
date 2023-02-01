package eu.qwan.vender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VendingMachineTest {
	private final VendingMachine machine = new VendingMachine();

	@Test
	public void Testchoiceless_machine_delivers_nothing() {
		assertEquals(Can.none, machine.deliver(Choice.cola));
		assertEquals(Can.none, machine.deliver(Choice.fanta));
	}

	@Test
	public void Testdelivers_can_of_choice() {
		machine.configure(Choice.cola, Can.cola, 10);
		machine.configure(Choice.fanta, Can.fanta, 10);
		machine.configure(Choice.sprite, Can.sprite, 10);
		assertEquals(Can.cola, machine.deliver(Choice.cola));
		assertEquals(Can.fanta, machine.deliver(Choice.fanta));
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
	}

	@Test
	public void Testdelivers_nothing_when_making_invalid_choice() {
		machine.configure(Choice.cola, Can.cola, 10);
		machine.configure(Choice.fanta, Can.fanta, 10);
		machine.configure(Choice.sprite, Can.sprite, 10);
		assertEquals(Can.none, machine.deliver(Choice.beer));
	}

	@Test
	public void Testdelivers_nothing_when_not_paid() {
		machine.configure(Choice.fanta, Can.fanta, 10, 2);
		machine.configure(Choice.sprite, Can.sprite, 10, 1);

		assertEquals(Can.none, machine.deliver(Choice.fanta));
	}

	@Test
	public void Testdelivers_fanta_when_paid() {
		machine.configure(Choice.sprite, Can.sprite, 10, 1);
		machine.configure(Choice.fanta, Can.fanta, 10, 2);

		machine.setValue(2);
		assertEquals(Can.fanta, machine.deliver(Choice.fanta));
		assertEquals(Can.none, machine.deliver(Choice.sprite));
	}

	@Test
	public void Testdelivers_sprite_when_paid() {
		machine.configure(Choice.sprite, Can.sprite, 10, 1);
		machine.configure(Choice.fanta, Can.fanta, 10, 2);

		machine.setValue(2);
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.none, machine.deliver(Choice.sprite));
	}

	@Test
	public void Testadd_payments() {
		machine.configure(Choice.sprite, Can.sprite, 10, 1);
		machine.configure(Choice.fanta, Can.fanta, 10, 2);

		machine.setValue(1);
		machine.setValue(1);
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.none, machine.deliver(Choice.sprite));
	}

	@Test
	public void Testreturns_change() {
		machine.configure(Choice.sprite, Can.sprite, 10, 1);
		machine.setValue(2);
		assertEquals(2, machine.getChange());
		assertEquals(0, machine.getChange());
	}

	@Test
	public void Teststock() {
		machine.configure(Choice.sprite, Can.sprite, 1, 0);
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.none, machine.deliver(Choice.sprite));
	}

	@Test
	public void Testadd_stock() {
		machine.configure(Choice.sprite, Can.sprite, 1, 0);
		machine.configure(Choice.sprite, Can.sprite, 1, 0);
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(Can.none, machine.deliver(Choice.sprite));
	}

	@Test
	public void Testcheckout_chip_if_chipknip_inserted() {
		machine.configure(Choice.sprite, Can.sprite, 1, 1);
		Chipknip chip = new Chipknip(10);
		machine.insertChip(chip);
		assertEquals(Can.sprite, machine.deliver(Choice.sprite));
		assertEquals(9, chip.credits);
	}

	@Test
	public void Testcheckout_chip_empty() {
		machine.configure(Choice.sprite, Can.sprite, 1, 1);
		Chipknip chip = new Chipknip(0);
		machine.insertChip(chip);
		assertEquals(Can.none, machine.deliver(Choice.sprite));
		assertEquals(0, chip.credits);
	}
}
