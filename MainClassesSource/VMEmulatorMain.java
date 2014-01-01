/********************************************************************************
 * The contents of this file are subject to the GNU General Public License      *
 * (GPL) Version 2 or later (the "License"); you may not use this file except   *
 * in compliance with the License. You may obtain a copy of the License at      *
 * http://www.gnu.org/copyleft/gpl.html                                         *
 *                                                                              *
 * Software distributed under the License is distributed on an "AS IS" basis,   *
 * without warranty of any kind, either expressed or implied. See the License   *
 * for the specific language governing rights and limitations under the         *
 * License.                                                                     *
 *                                                                              *
 * This file was originally developed as part of the software suite that        *
 * supports the book "The Elements of Computing Systems" by Nisan and Schocken, *
 * MIT Press 2005. If you modify the contents of this file, please document and *
 * mark your changes clearly, for the benefit of others.                        *
 ********************************************************************************/

import Hack.Controller.*;
import Hack.VMEmulator.*;
import HackGUI.*;
import SimulatorsGUI.*;
import builtInVMCode.Sys;

import javax.swing.*;
import java.net.URL;
import java.util.Arrays;

/**
 * The VM Emulator.
 */
public class VMEmulatorMain
{
  /**
   * The command line VM Emulator program.
   */
  public static void main(String[] args)
  {
    Sys.mainArgs = args;

    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
    }

    VMEmulatorComponent simulatorGUI = new VMEmulatorComponent();
    ControllerGUI controllerGUI = new ControllerComponent();
    VMEmulatorApplication application =
        new VMEmulatorApplication(controllerGUI, simulatorGUI, "bin/scripts/defaultVM.txt",
                                  "bin/help/vmUsage.html", "bin/help/vmAbout.html");

    if (args.length > 0) {
        Sys.mainArgs = Arrays.copyOfRange(args, 1, args.length);

        if (args[0].endsWith(".tst")) {
          new HackController(new VMEmulator(), args[0]);

        }
        else {
            simulatorGUI.loadProgram(args[0]);
        }
    }

  }
}

