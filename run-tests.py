import json
import os
import shutil
import subprocess
import platform
from pathlib import Path
from typing import TypedDict



def delete_contents_of_directory(directory: Path) -> None:
	for path in directory.iterdir():
		if path.is_file():
			path.unlink()
		elif path.is_dir():
			shutil.rmtree(path)


class EnvironmentResource(TypedDict):
	source: str
	target: str

# Pathways / Directories
gradle_command = "gradlew.bat" if platform.system() == "Windows" else "./gradlew"
workspace_directory = Path("/github/workspace")
if not workspace_directory.exists():
	workspace_directory = Path(__file__).parent.resolve()
plugins_storage = workspace_directory / "build" / "extra-plugins"
skript_repo_git_url = "https://github.com/SkriptLang/Skript.git"
skript_repo_path = workspace_directory / "build/Skript"
skript_test_directory = skript_repo_path / "src" / "test" / "skript" / "tests"
custom_test_directory = skript_test_directory / "custom"
environments_dir = skript_repo_path / "src" / "test" / "skript" / "environments"

# User Settings
skript_test_action = os.environ.get("INPUT_SKRIPT_TEST_ACTION", "quickTest")
test_script_directory = workspace_directory / os.environ['INPUT_TEST_SCRIPT_DIRECTORY']
skript_repo_ref = os.environ.get("INPUT_SKRIPT_REPO_REF", None)
run_vanilla_tests = os.environ.get("INPUT_RUN_VANILLA_TESTS", "true") == "true"
addon_jar_path = workspace_directory / os.environ["INPUT_ADDON_JAR_PATH"]

# Determine Extra Plugins
extra_plugins_directory = None
extra_plugins_directory_string = os.environ.get("INPUT_EXTRA_PLUGINS_DIRECTORY", None)
if extra_plugins_directory_string is not None and extra_plugins_directory_string != "":
	extra_plugins_directory = workspace_directory / extra_plugins_directory_string


print("Configuration:")
print(f"  Test script directory: {test_script_directory}")
print(f"  Skript repo ref: {skript_repo_ref}")
print(f"  Run vanilla tests: {run_vanilla_tests}")
print(f"  Extra plugins directory: {extra_plugins_directory}")

# Clone Skripts Repo
subprocess.run(("git", "clone", "--recurse-submodules", skript_repo_git_url, str(skript_repo_path)))
os.chdir(skript_repo_path)
if skript_repo_ref is not None and not skript_repo_ref.isspace():
	subprocess.run(("git", "checkout", "-f", skript_repo_ref))
	subprocess.run(("git", "submodule", "update", "--recursive"))

if not run_vanilla_tests:
	print("Deleting vanilla tests")
	delete_contents_of_directory(skript_test_directory)

for environment_file_path in environments_dir.glob("**/*.json"):
	with open(environment_file_path, "r") as environment_file:
		environment = json.load(environment_file)
		if "resources" not in environment:
			environment["resources"] = []
		resources = environment["resources"]
		resources.append(EnvironmentResource(
			source=str(addon_jar_path.absolute().resolve()),
			target=f"plugins/{addon_jar_path.name}"
		))
		if plugins_storage.exists():
			for plugin_path in plugins_storage.iterdir():
				resources.append(EnvironmentResource(
					source=str(plugin_path.absolute().resolve()),
					target=f"plugins/{plugin_path.name}"
				))
	with open(environment_file_path, "w") as environment_file:
		json.dump(environment, environment_file)

shutil.rmtree(custom_test_directory, ignore_errors=True)
shutil.copytree(test_script_directory, custom_test_directory)
subprocess.run((gradle_command, "clean"))
gradle_test_process = subprocess.run((gradle_command, skript_test_action))
exit(gradle_test_process.returncode)
